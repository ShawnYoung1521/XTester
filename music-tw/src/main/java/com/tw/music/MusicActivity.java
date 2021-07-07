package com.tw.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import cn.xy.library.util.log.XLog;

public class MusicActivity extends Activity {
    private static final String TAG = "MusicActivity";
	private static final boolean DBG = false;

	private TWMusic mTW = null;
	private boolean startEQ = false;

	private ImageView mAlbumArt;
	private ListView mList;

    private Record mSDRecord;
    private Record mUSBRecord;
    private Record mMediaRecord;
    private Record mCList;

    private void loadVolume(Record record, String volume) {
    	if ((record != null) && (volume != null)) {
     		try {
     	   		BufferedReader br = null;
    			try {
    				String xpath = null;
    				if(volume.startsWith("/storage/usb") || volume.startsWith("/storage/extsd")) {
    					xpath = "/data/tw/" + volume.substring(9);
    				} else {
    					xpath = volume + "/DCIM";
    				}
    				br = new BufferedReader(new FileReader(xpath + "/.music"));
    				String path = null;
					XLog.a(br.readLine());
    				ArrayList<LName> l = new ArrayList<LName>();
    				while((path = br.readLine()) != null) {
    					File f = new File(volume + "/" + path);
    					if (f.canRead() && f.isDirectory()) {
    						String n = f.getName();
    						String p = f.getAbsolutePath();
							XLog.a(n);
    						if(n.equals(".")) {
    							String p2 = p.substring(0, p.lastIndexOf("/"));
    							String p3 = p2.substring(p2.lastIndexOf("/") + 1);
    							l.add(new LName(p3, p));
    						} else {
        						l.add(new LName(n, p));
    						}
    					}
    				}
    				record.setLength(l.size());
    				for(LName n : l) {
    					record.add(n);
    				}
    				l.clear();
    			} catch (Exception e) {
    			} finally {
    				if(br != null) {
    					br.close();
    					br = null;
    				}
    			}
			} catch (Exception e) {
			}
    	}
    }

    private void addRecordSD(String path) {
		for(Record r : mSDRecordArrayList) {
			if(path.equals(r.mName)) {
				return;
			}
		}
		Record r = new Record(path, 1, 0);
		loadVolume(r, path);
		mSDRecordArrayList.add(r);
		if((mCList != null) && mCList.mName.equals("SD")) {
			mCList = mSDRecordArrayList.get(0);
		}
    }

    private void addRecordUSB(String path) {
		for(Record r : mUSBRecordArrayList) {
			if(path.equals(r.mName)) {
				return;
			}
		}
		Record r = new Record(path, 2, 0);
		loadVolume(r, path);
		mUSBRecordArrayList.add(r);
		if((mCList != null) && mCList.mName.equals("USB")) {
			mCList = mUSBRecordArrayList.get(0);
		}
    }

    private void removeRecordSD(String path) {
		for(Record r : mSDRecordArrayList) {
			if(path.equals(r.mName)) {
				Record t = mCList;
				if(mCList.mLevel == 1) {
					t = mCList.mPrev;
				}
				String s = t.mName;
				r.clearRecord();
				mSDRecordArrayList.remove(r);
				if(mSDRecordLevel >= mSDRecordArrayList.size()){
					mSDRecordLevel = mSDRecordArrayList.size() - 1;
					if(mSDRecordLevel < 0) {
						mSDRecordLevel = 0;
					}
				}
				if(path.equals(s)) {
	    			if(mSDRecordArrayList.size() > 0) {
	    				mCList = mSDRecordArrayList.get(mSDRecordLevel);
	    			} else {
	        			mCList = mSDRecord;
	    			}
				}
				return;
			}
		}
    }

    private void removeRecordUSB(String path) {
		for(Record r : mUSBRecordArrayList) {
			if(path.equals(r.mName)) {
				Record t = mCList;
				if(mCList.mLevel == 1) {
					t = mCList.mPrev;
				}
				String s = t.mName;
				r.clearRecord();
				mUSBRecordArrayList.remove(r);
				if(mUSBRecordLevel >= mUSBRecordArrayList.size()){
					mUSBRecordLevel = mUSBRecordArrayList.size() - 1;
					if(mUSBRecordLevel < 0) {
						mUSBRecordLevel = 0;
					}
				}
				if(path.equals(s)) {
	    			if(mUSBRecordArrayList.size() > 0) {
	    				mCList = mUSBRecordArrayList.get(mUSBRecordLevel);
	    			} else {
	        			mCList = mUSBRecord;
	    			}
				}
				return;
			}
		}
    }

    private void initRecord() {
    	mSDRecord = new Record("SD", 1, 0);
    	File[] fileSD = new File("/storage").listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				String n = f.getName();
				if(f.canRead() && f.isDirectory() && n.startsWith("extsd")) {
					return true;
				}
				return false;
			}
    	});
    	if(fileSD != null) {
        	for(File f : fileSD) {
        		addRecordSD(f.getAbsolutePath());
        	}
    	}
    	mUSBRecord = new Record("USB", 2, 0);
    	File[] fileUSB = new File("/storage").listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				String n = f.getName();
				if(f.canRead() && f.isDirectory() && n.startsWith("usb")) {
					return true;
				}
				return false;
			}
    	});
    	if(fileUSB != null) {
        	for(File f : fileUSB) {
        		addRecordUSB(f.getAbsolutePath());
        	}
    	}
    	mMediaRecord = new Record("iNand", 3, 0);
    	loadVolume(mMediaRecord, "/mnt/sdcard/iNand");
    	mCList = mTW.mPlaylistRecord;
    	if (mCList.mCLength == 0) {
			if(mSDRecordArrayList.size() > 0) {
				mCList = mSDRecordArrayList.get(0);
			} else {
    			mCList = mSDRecord;
			}
	    	if (mCList.mCLength == 0) {
    			if(mUSBRecordArrayList.size() > 0) {
    				mCList = mUSBRecordArrayList.get(0);
    			} else {
        			mCList = mUSBRecord;
    			}
		    	if (mCList.mCLength == 0) {
		    		mCList = mMediaRecord;
			    	if (mCList.mCLength == 0) {
			    		mCList = mTW.mPlaylistRecord;
			    	}
		    	}
	    	}
    	}
    }

	private void seekTo(int msec) {
    	if(mService != null) {
        	mService.seekTo(msec);
    	}
	}

	private void current(int pos, boolean r) {
    	if(mService != null) {
        	mService.current(pos, r);
    	}
    }

    private void next() {
    	if(mService != null) {
        	mService.next();
    	}
    }

    private void prev() {
    	if(mService != null) {
        	mService.prev();
    	}
    }

	private static final int[] TR_ID = new int[] {R.id.playlist, R.id.sd, R.id.usb, R.id.media};

	private MusicService mService = null;
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MusicService.MusicBinder)service).getService();
			mService.setAHandler(mHandler);
			if(!mService.isPlaying()) {
				mService.start();
				mService.seekTo(mTW.mCurrentPos);
				mService.duck(false);
			}
			mHandler.sendEmptyMessage(NOTIFY_CHANGE);
		}
	};

	private static final int NOTIFY_CHANGE = 0xff01;
    private static final int SHOW_PROGRESS = 0xff02;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			try {
			switch(msg.what) {
			case 0x0201: {
				if (onResume) {
					switch (msg.arg2) {
					//case 0x05://
					case 0x3a://keyUp
						prev();
						break;
					//case 0x06://
					case 0x3b://keyDown
						next();
						break;
					case 0x3c://keyLeft
						selectButton(true, false);
						break;
					case 0x3d://keyRight
						selectButton(false, true);
						break;
					case 0x3f://keyOK
						bottomViewList.get(bottomViewIndex).performClick();
						bottomViewList.get(bottomViewIndex).clearFocus();
						bottomViewList.get(bottomViewIndex).requestFocusFromTouch();
						break;
					}
				}
				break;
			}
			case TWMusic.RETURN_MOUNT: {
				String volume = null;
				switch(msg.arg1) {
				case 1:
					volume = "/storage/" + msg.obj;
					if(msg.arg2 == 0) {
						removeRecordSD(volume);
					} else {
						addRecordSD(volume);
					}
					showTRText(R.id.sd);
					break;
				case 2:
					volume = "/storage/" + msg.obj;
					if(msg.arg2 == 0) {
						removeRecordUSB(volume);
					} else {
						addRecordUSB(volume);
					}
					showTRText(R.id.usb);
					break;
				case 3:
					volume = "/mnt/sdcard/iNand";
					if(msg.arg2 == 0) {
						mMediaRecord.clearRecord();
					} else {
						loadVolume(mMediaRecord, volume);
					}
					break;
				}
				if((mTW.mCurrentPath != null) && mTW.mCurrentPath.startsWith(volume)) {
					if(msg.arg2 != 0) {
						if((mService != null) && !mService.isPlaying() && (mTW.getService() == TWMusic.ACTIVITY_RUSEME)) {
							mService.start();
							mService.seekTo(mTW.mCurrentPos);
							mService.duck(false);
						}
					}
				}
				mAdapter.notifyDataSetChanged();
				break;
			}
			case NOTIFY_CHANGE:
				if(mService != null) {
					((ImageView)findViewById(R.id.pp)).getDrawable().setLevel(mService.isPlaying() ? 1 : 0);
			        CharSequence artistName = mService.getArtistName();
			        CharSequence albumName = mService.getAlbumName();
			        CharSequence titleName = mService.getTrackName();
			        if(artistName == null) {
			        	artistName = getString(R.string.unknown);
			        }
			        if(albumName == null) {
			        	albumName = getString(R.string.unknown);
			        }
			        if(titleName == null) {
			        	titleName = mService.getFileName();
			        	if(titleName == null) {
			        		titleName = getString(R.string.unknown);
			        	}
			        }
					((TextView)findViewById(R.id.artist)).setText(artistName);
					((TextView)findViewById(R.id.album)).setText(albumName);
					((TextView)findViewById(R.id.song)).setText(titleName);
					Bitmap bm = mService.getAlbumArt();
					if(bm == null) {
						mAlbumArt.setImageResource(R.drawable.album);
					} else {
						mAlbumArt.setImageBitmap(bm);
					}
					mAdapter.notifyDataSetChanged();
					mList.smoothScrollToPosition(mTW.mCurrentIndex + mCList.mLevel);
				}
				break;
			case SHOW_PROGRESS:
				if(mService != null) {
					int duration = mService.getDuration();
					int position = mService.getCurrentPosition();
					if(duration < 0) {
						duration = 0;
					}
					if(position < 0) {
						position = 0;
					}
					if(position > duration){
						return;
					} else {
						int totaltime = duration / 1000;
						int stotaltime = totaltime;
						int mtotaltime = stotaltime / 60;
						int htotaltime = mtotaltime / 60;
						stotaltime %= 60;
						mtotaltime %= 60;
						htotaltime %= 24;
						if(htotaltime == 0) {
							((TextView)findViewById(R.id.totaltime)).setText(String.format(Locale.US, "%d:%02d", mtotaltime, stotaltime));
						} else {
							((TextView)findViewById(R.id.totaltime)).setText(String.format(Locale.US, "%d:%02d:%02d", htotaltime, mtotaltime, stotaltime));
						}
						int currenttime = position / 1000;
						int scurrenttime = currenttime;
						int mcurrenttime = scurrenttime / 60;
						int hcurrenttime = mcurrenttime / 60;
						scurrenttime %= 60;
						mcurrenttime %= 60;
						hcurrenttime %= 24;
						if(hcurrenttime == 0) {
							((TextView)findViewById(R.id.currenttime)).setText(String.format(Locale.US, "%d:%02d", mcurrenttime, scurrenttime));
						} else {
							((TextView)findViewById(R.id.currenttime)).setText(String.format(Locale.US, "%d:%02d:%02d", hcurrenttime, mcurrenttime, scurrenttime));
						}
					}
					ProgressBar progress = (ProgressBar)findViewById(R.id.progress);
					progress.setMax(duration);
					progress.setProgress(position);
				}
				break;
			}
		} catch (Exception e) {
		}
		}
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTW = TWMusic.open();
		mTW.addHandler(TAG, mHandler);
        initRecord();
        setContentView(R.layout.music);
        startService(new Intent(this, MusicService.class));
        mAlbumArt = (ImageView)findViewById(R.id.albumart);
        mList = (ListView)findViewById(R.id.list);
        mAdapter = new MyListAdapter(this);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if((mCList.mLevel != 0) && (position == 0)){
					mCList = mCList.mPrev;
				} else {
					if(mCList.mLevel != 0) {
						position--;
					}
					if((mCList.mLevel == 0) && (mCList.mIndex != 0)) {
						Record r = mCList.getNext(position);
						if(r == null) {
							r = new Record(mCList.mLName[position].mName, position, mCList.mLevel + 1, mCList);
							mTW.loadFile(r, mCList.mLName[position].mPath);
						}
						mCList.setNext(r);
						mCList = r;
					} else {
						mTW.mCurrentIndex = position;
						mTW.mCurrentAPath = mCList.mLName[position].mPath;
						String path = mTW.mCurrentAPath.substring(0, mTW.mCurrentAPath.lastIndexOf("/"));
						if((path != null) && !path.equals(mTW.mCurrentPath)) {
							if(mCList.mLevel == 1) {
								mTW.mPlaylistRecord.copyLName(mCList);
							}
						}
						mTW.toRPlaylist(position);
						mTW.mCurrentPath = path;
						current(0, false);
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});
        ((SeekBar)findViewById(R.id.progress)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser) {
					seekTo(progress);
				}
			}
		});
        showTRText(R.id.sd);
        showTRText(R.id.usb);
        ((TextView)findViewById(TR_ID[mCList.mIndex])).getCompoundDrawables()[0].setLevel(1);
        ((ImageView)findViewById(R.id.shuffle)).getDrawable().setLevel(mTW.mShuffle);
        ((ImageView)findViewById(R.id.repeat)).getDrawable().setLevel(mTW.mRepeat);
        bindService(new Intent(this, MusicService.class), mConnection, BIND_AUTO_CREATE);
        initBottomView();
    }

	@Override
	protected void onDestroy() {
		mTW.requestSource(false);
		if(mService != null) {
			mService.setAHandler(null);
		}
		try {
			unbindService(mConnection);
		} catch (Exception e) {
		}
		mTW.close();
		mTW = null;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		onResume=true;
		startEQ = false;
		mTW.requestService(TWMusic.ACTIVITY_RUSEME);
		if((mService != null) && !mService.isPlaying()) {
			mService.start();
			mService.seekTo(mTW.mCurrentPos);
			mService.duck(false);
		}
		if (mTW!=null) {
			ivRepeat.getDrawable().setLevel(mTW.mRepeat);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if(!startEQ) mTW.requestService(TWMusic.ACTIVITY_PAUSE);
		onResume=false;
		super.onPause();
	}

	private MyListAdapter mAdapter;

    private class MyListAdapter extends BaseAdapter {
		public MyListAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			if(mCList == null) {
				return 0;
			} else if(mCList.mLevel == 0) {
				return mCList.mCLength;
			} else {
				return mCList.mCLength + 1;
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v;
			if(convertView == null) {
				v = newView(parent);
			} else {
				v = convertView;
			}
			bindView(v, position, parent);
			return v;
		}

	    private class ViewHolder {
//	        ImageView icon;
	        TextView line;
	        ImageView play_indicator;
	    }

	    private View newView(ViewGroup parent) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.tl_list, parent, false);
            ViewHolder vh = new ViewHolder();
//            vh.icon = (ImageView) v.findViewById(R.id.icon);
            vh.line = (TextView) v.findViewById(R.id.line);
            vh.play_indicator = (ImageView) v.findViewById(R.id.play_indicator);
            v.setTag(vh);
            return v;
		}

	    private void bindView(View v, int position, ViewGroup parent) {
			ViewHolder vh = (ViewHolder) v.getTag();
			String name, path;
			if(mCList.mLevel == 0) {
				name = mCList.mLName[position].mName;
				path = mCList.mLName[position].mPath;
			} else if(position == 0) {
				name = mCList.mName;
				path = null;
			} else {
				name = mCList.mLName[position - 1].mName;
				path = mCList.mLName[position - 1].mPath;
			}
			vh.line.setText(name);
			if((mCList.mLevel != 0) && (position == 0)) {
				((RelativeLayout)v).getBackground().setLevel(1);
				vh.play_indicator.getDrawable().setLevel(2);
			} else {
				((RelativeLayout)v).getBackground().setLevel(0);
				if((mCList.mLevel == 1) || (mCList.mIndex == 0)) {
					vh.play_indicator.getDrawable().setLevel(0);
					if((path != null) && path.equals(mTW.mCurrentAPath)) {
						((RelativeLayout)v).getBackground().setLevel(2);
					}
				} else {
					vh.play_indicator.getDrawable().setLevel(1);
					if((path != null) && path.equals(mTW.mCurrentPath)) {
						((RelativeLayout)v).getBackground().setLevel(1);
					}
				}
			}
		}

        private Context mContext;
    }

    private int getTypeIndex(int id) {
    	switch(id) {
    	case R.id.playlist:
    		return 0;
    	case R.id.sd:
    		return 1;
    	case R.id.usb:
    		return 2;
    	case R.id.media:
    		return 3;
    	default:
    		return -1;
    	}
    }

    private ArrayList<Record> mSDRecordArrayList = new ArrayList<Record>();
    private ArrayList<Record> mUSBRecordArrayList = new ArrayList<Record>();
    private int mSDRecordLevel = 0;
    private int mUSBRecordLevel = 0;
    
    private void showTRText(int id) {
    	TextView tv = (TextView)findViewById(id);
    	switch(id) {
    	case R.id.sd:
	    	if(mSDRecordArrayList.size() > 1) {
	    		String n = mSDRecordArrayList.get(mSDRecordLevel).mName;
	    		tv.setText("extSD" + n.charAt(n.length() - 1));
	    	} else {
	    		tv.setText(getResources().getString(R.string.sd));
	    	}
    		break;
    	case R.id.usb:
	    	if(mUSBRecordArrayList.size() > 1) {
	    		String n = mUSBRecordArrayList.get(mUSBRecordLevel).mName;
	    		tv.setText("USB" + n.charAt(n.length() - 1));
	    	} else {
	    		tv.setText("USB");
	    	}
    		break;
    	}
    }

    public void onTypeClick(View v) {
		try{
    	int id = v.getId();
    	Record r = mCList;
    	if(r.mLevel == 1) {
    		r = r.mPrev;
    	}
    	if(r.mIndex != getTypeIndex(id)) {
    		((TextView)findViewById(TR_ID[r.mIndex])).getCompoundDrawables()[0].setLevel(0);
    		switch(id) {
    		case R.id.playlist:
    			mCList = mTW.mPlaylistRecord;
    			break;
    		case R.id.sd:
    			if(mSDRecordArrayList.size() > 0) {
	    			if(mSDRecordLevel >= mSDRecordArrayList.size()) {
	    				mSDRecordLevel = 0;
	    			}
    				mCList = mSDRecordArrayList.get(mSDRecordLevel);
    			} else {
        			mCList = mSDRecord;
    			}
    			break;
    		case R.id.usb:
    			if(mUSBRecordArrayList.size() > 0) {
	    			if(mUSBRecordLevel >= mUSBRecordArrayList.size()) {
	    				mUSBRecordLevel = 0;
	    			}
    				mCList = mUSBRecordArrayList.get(mUSBRecordLevel);
    			} else {
        			mCList = mUSBRecord;
    			}
    			break;
    		case R.id.media:
    			mCList = mMediaRecord;
    			break;
    		}
    		((TextView)findViewById(TR_ID[mCList.mIndex])).getCompoundDrawables()[0].setLevel(1);
    	} else {
    		switch(id) {
    		case R.id.sd:
    			if(mSDRecordArrayList.size() > 0) {
	    			if(++mSDRecordLevel >= mSDRecordArrayList.size()) {
	    				mSDRecordLevel = 0;
	    			}
    				mCList = mSDRecordArrayList.get(mSDRecordLevel);
    			} else {
    				mCList = mSDRecord;
    			}
    			break;
    		case R.id.usb:
    			if(mUSBRecordArrayList.size() > 0) {
	    			if(++mUSBRecordLevel >= mUSBRecordArrayList.size()) {
	    				mUSBRecordLevel = 0;
	    			}
    				mCList = mUSBRecordArrayList.get(mUSBRecordLevel);
    			} else {
        			mCList = mUSBRecord;
    			}
    			break;
    		}
    		showTRText(id);
    	}
		mAdapter.notifyDataSetChanged();
		} catch (Exception e) {
		}
    }

	public void onClick(View v) {
    	switch(v.getId()) {
    	case R.id.home: {
			Intent it = new Intent(Intent.ACTION_MAIN);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.addCategory(Intent.CATEGORY_HOME);
			startActivity(it);
			break;
    	}
    	case R.id.shuffle:
    		int repeatImage =ivRepeat.getDrawable().getLevel();
    		repeatImage++;
            if (repeatImage>3) {
            	repeatImage=0;
                mTW.mRepeat=1;
                mTW.mShuffle = 0;
            }else {
                if (repeatImage==1) {
                    mTW.mRepeat = 1;
                    mTW.mShuffle = 0;
                }else if (repeatImage==2){
                    mTW.mShuffle = 0;
                    mTW.mRepeat=2;
                }else {
                    mTW.mShuffle = 1;
                    mTW.mRepeat=3;
                }
            }
            mTW.toRPlaylist(mTW.mCurrentIndex);
    		ivRepeat.getDrawable().setLevel(mTW.mRepeat);
    		break;
    	case R.id.pp:
    		if(mService != null) {
    			if(mService.isPlaying()) {
    				mService.pause();
    				((ImageView)findViewById(R.id.pp)).getDrawable().setLevel(0);
    			} else {
    				mService.start();
    			}
    		}
    		break;
    	case R.id.prev:
    		prev();
    		break;
    	case R.id.next:
    		next();
    		break;
    	case R.id.repeat:
    		if(++mTW.mRepeat > 2) {
    			mTW.mRepeat = 1;
    		}
    		((ImageView)findViewById(R.id.repeat)).getDrawable().setLevel(mTW.mRepeat);
    		break;
    	case R.id.eq:
			try {
				Intent it = new Intent();
				it.setClassName("com.tw.eq", "com.tw.eq.EQActivity");
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(it);
				startEQ = true;
			} catch (Exception e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
    		break;
    	case R.id.back:
    		finish();
    		break;
    	}
    }
	
	private boolean onResume=false;
	
	private int bottomViewIndex = -1;
	private ImageView ivHome;
	private ImageView ivShuffle;
	private ImageView ivPrev;
	private ImageView ivPP;
	private ImageView ivNext;
	private ImageView ivRepeat;
	private TextView tvEQ;
	private ImageView ivBack;
	private List<View> bottomViewList = new ArrayList<View>();
	
	private void selectButton(boolean isKeyLeft, boolean isKeyRight){
    	if(isKeyRight){
    		bottomViewIndex++;
    		if(bottomViewIndex > bottomViewList.size()-1) {
    			bottomViewIndex = 0;
    		}
    		bottomViewList.get(bottomViewIndex).requestFocus();
    		bottomViewList.get(bottomViewIndex).requestFocusFromTouch();
    	}
    	
    	if(isKeyLeft){
    		bottomViewIndex--;
    		if(bottomViewIndex < 0) {
    			bottomViewIndex = bottomViewList.size()-1;
    		}
    		bottomViewList.get(bottomViewIndex).requestFocus();
    		bottomViewList.get(bottomViewIndex).requestFocusFromTouch();
    	}
    }
	private void initBottomView() {
		ivHome = (ImageView) findViewById(R.id.home);
		ivShuffle = (ImageView) findViewById(R.id.shuffle);
		ivPrev = (ImageView) findViewById(R.id.prev);
		ivPP = (ImageView) findViewById(R.id.pp);
		ivNext = (ImageView) findViewById(R.id.next);
		ivRepeat = (ImageView) findViewById(R.id.shuffle);
		tvEQ = (TextView) findViewById(R.id.eq);
		ivBack = (ImageView) findViewById(R.id.back);
		
		bottomViewList.add(ivHome);
		bottomViewList.add(ivShuffle);
		bottomViewList.add(ivPrev);
		bottomViewList.add(ivPP);
		bottomViewList.add(ivNext);
		bottomViewList.add(ivRepeat);
		bottomViewList.add(tvEQ);
		bottomViewList.add(ivBack);
	}
}