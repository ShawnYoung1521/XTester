package com.tw.music;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class MusicService extends Service {
	private static final String TAG = "MusicService";

	private final IBinder mBinder = new MusicBinder();

	public class MusicBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private TWMusic mTW = null;

	private MediaPlayer mMediaPlayer;
	private Handler mAHandler = null;

	private static final int NOTIFY_CHANGE = 0xff01;
    private static final int SHOW_PROGRESS = 0xff02;
    private static final int NEXT = 0xff03;
    private static final int PREV = 0xff04;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case NEXT:
				_next();
				break;
			case PREV:
				_prev();
				break;
			case TWMusic.RETURN_MOUNT: {
				String volume = null;
				switch(msg.arg1) {
				case 1:
					volume = "/storage/" + msg.obj;
					break;
				case 2:
					volume = "/storage/" + msg.obj;
					break;
				case 3:
					volume = "/mnt/sdcard/iNand";
					break;
				}
				if((mTW.mCurrentPath != null) && mTW.mCurrentPath.startsWith(volume)) {
					if(msg.arg2 == 0) {
						mTW.mPlaylistRecord.clearRecord();
						stop();
					} else if(!isPlaying()){
						mTW.loadFile(mTW.mPlaylistRecord, mTW.mCurrentPath);
						mTW.toRPlaylist(mTW.mCurrentIndex);
				    	if(prepare(mTW.mCurrentAPath) == 0) {
				    		seekTo(mTW.mCurrentPos);
				    	}
					}
				}
				if(mAHandler != null) {
					mAHandler.obtainMessage(msg.what, msg.arg1, msg.arg2, msg.obj).sendToTarget();
				}
				break;
			}
			case SHOW_PROGRESS:
				if(isPlaying()) {
					int duration = getDuration();
					int position = getCurrentPosition();
					mTW.mCurrentPos = mMediaPlayer.getCurrentPosition(); //added by XY for usb-memory
					if(duration < 0) {
						duration = 0;
					}
					if(position < 0) {
						position = 0;
					}
					int currenttime = position / 1000;
					int scurrenttime = currenttime;
					int mcurrenttime = scurrenttime / 60;
					int hcurrenttime = mcurrenttime / 60;
					scurrenttime %= 60;
					mcurrenttime %= 60;
					hcurrenttime %= 24;
					if((duration > 0) && (position <= duration)) {
						int percent = position * 100 / duration;
						mTW.media(0, mTW.mCurrentIndex + 1, mTW.mPlaylistRecord.mCLength, (hcurrenttime<<16) | (mcurrenttime<<8) | scurrenttime, percent);
						mTW.write(0x9f00, 0x03, (isPlaying() ? 0x80 : 0x00) | (percent & 0x7f),getTrackName());
						mTW.write(0x0303, 0x03, (isPlaying() ? 0x80 : 0x00) | (percent & 0x7f));
					}
					if(mAHandler != null) {
						mAHandler.sendEmptyMessage(SHOW_PROGRESS);
					}
					mHandler.removeMessages(SHOW_PROGRESS);
					mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
				}
				break;
			}
		}
    };


	private int mHintsLengh=7;
	private boolean isError=false;
	private long[] mHints = new long[mHintsLengh];//初始全部为0
	@Override
	public void onCreate() {
		super.onCreate();
		mTW = TWMusic.open();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				next();
			}
		});
        mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if (mHints[mHints.length - 1]>0) {//得初始化一次
					if (SystemClock.uptimeMillis()-mHints[mHints.length - 1]<=700) {//如果两次间隔时间不超过700毫秒，记录当前这次次数
							mHints = Arrays.copyOf(mHints, mHints.length - 1);
							//获得当前系统已经启动的时间
							mHints[mHints.length - 1] = SystemClock.uptimeMillis();
							if(SystemClock.uptimeMillis()-mHints[0]<=5000){
							    mHints = new long[mHintsLengh];//初始全部为0
							    isError=true;
							    Toast.makeText(getApplicationContext(),getString(R.string.error), Toast.LENGTH_LONG).show();
							}
					}else {
						mHints = new long[mHintsLengh];//初始全部为0
					}
				}else {
					//获得当前系统已经启动的时间
					mHints[mHints.length - 1] = SystemClock.uptimeMillis();
				}
				return true;
			}
		});
    	if(prepare(mTW.mCurrentAPath) == 0) {
    		seekTo(mTW.mCurrentPos);
    	}
    	mTW.addHandler(TAG, mHandler);
	}

	@Override
	public void onDestroy() {
		mTW.removeHandler(TAG);
		save();
		mHandler.removeMessages(SHOW_PROGRESS);
		mMediaPlayer.release();
		mMediaPlayer = null;
		mTW.requestSource(false);
		mTW.close();
		mTW = null;
		super.onDestroy();
	}

	private int prepare(String path) {
		mMediaPlayer.stop();
		mHandler.removeMessages(SHOW_PROGRESS);
		mMediaPlayer.reset();
        try {
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.prepare();
			return 0;
		} catch (IllegalArgumentException e) {
			return -1;
		} catch (IllegalStateException e) {
			return -2;
		} catch (IOException e) {
			return -3;
		} catch (Exception e) {
			return -4;
		}
	}

	public void start() {
		mTW.requestSource(true);
		mMediaPlayer.start();
		mHandler.removeMessages(SHOW_PROGRESS);
		mHandler.sendEmptyMessage(SHOW_PROGRESS);
		retriever(mTW.mCurrentAPath);
		notifyChange();
	}

	public void seekTo(int msec) {
		if(mMediaPlayer.isPlaying()) {
			int duration = mMediaPlayer.getDuration();
			if((msec > 0) && (duration > 0) && (msec < duration)) {
				mMediaPlayer.seekTo(msec);
			}
		}
	}

	public void pause() {
		mTW.mCurrentPos = mMediaPlayer.getCurrentPosition();
		mMediaPlayer.pause();
		mHandler.removeMessages(SHOW_PROGRESS);
		notifyChange();
	}

	public boolean isPlaying() {
		return mMediaPlayer.isPlaying();
	}

	public void stop() {
		mMediaPlayer.stop();
		mHandler.removeMessages(SHOW_PROGRESS);
		mTW.mCurrentArtist = null;
		mTW.mCurrentAlbum = null;
		mTW.mCurrentSong = null;
		notifyChange();
	}

	private void retriever(String path) {
		mTW.mCurrentArtist = null;
		mTW.mCurrentAlbum = null;
		mTW.mCurrentSong = null;
		if(mTW.mAlbumArt != null) {
			mTW.mAlbumArt = null;
		}
		MediaMetadataRetriever r = new MediaMetadataRetriever();
		try {
			r.setDataSource(path);
			if(r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) != null) {
				mTW.mCurrentArtist = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
				mTW.mCurrentAlbum = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
				mTW.mCurrentSong = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                try {
                              if(TextUtils.isEmpty(mTW.mCurrentSong)){
                                    mTW.mCurrentSong = getFileName();
                              }
                              if(TextUtils.isEmpty(mTW.mCurrentArtist)){
                                    mTW.mCurrentArtist = " ";
                              }
                              if(TextUtils.isEmpty(mTW.mCurrentAlbum)){
                                    mTW.mCurrentAlbum = " ";
                              }
                     final byte[] song =  mTW.mCurrentSong.getBytes("UTF-16LE");
                     final byte[] singer =  mTW.mCurrentArtist.getBytes("UTF-16LE");
                     final byte[] album =  mTW.mCurrentAlbum.getBytes("UTF-16LE");
                     mTW.write(0x0510, 0x13, song.length, song);
                     mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                          // TODO Auto-generated method  stub
                                          mTW.write(0x0510, 0x03,  singer.length, singer);
                                    }
                              }, 100);
                     mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                          // TODO Auto-generated method  stub
                                          mTW.write(0x0510, 0x23,  album.length, album);
                                    }
                              }, 200);
               } catch (Exception e) {
                     Log.e(TAG, Log.getStackTraceString(e));
               }
				byte albumArt[] = r.getEmbeddedPicture();
				if(albumArt != null) {
					mTW.mAlbumArt = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					mTW.mAlbumArt.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					albumArt = bos.toByteArray();
				}else{
					albumArt = null;
				}
				
				Intent intent = new Intent("com.tw.launcher.musicinfo");
                intent.putExtra("msg_Artist", mTW.mCurrentArtist);
                intent.putExtra("msg_Song", mTW.mCurrentSong);
                intent.putExtra("msg_path", path);
				if (albumArt != null && albumArt.length < 800*800) {
					intent.putExtra("msg_Albumart", albumArt);
				}else{
					byte albumArt1[] = new byte[] {};
					intent.putExtra("msg_Albumart", albumArt1);
				}
                sendBroadcast(intent);
			}
		} catch (Exception e) {
		}
		r.release();
	}

    private void rew() {
    	if(mMediaPlayer.isPlaying()) {
        	int pos = mMediaPlayer.getCurrentPosition();
        	pos -= 10000;
        	if(pos > 0 && pos < mMediaPlayer.getDuration()) {
            	mMediaPlayer.seekTo(pos);
        	}
    	}
    }

    private void ffwd() {
    	if(mMediaPlayer.isPlaying()) {
        	int pos = mMediaPlayer.getCurrentPosition();
        	pos += 15000;
        	if(pos > 0 && pos < mMediaPlayer.getDuration()) {
            	mMediaPlayer.seekTo(pos);
        	}
    	}
    }

    private boolean play(int pos) {
    	if(mTW.mCurrentIndex > -1 && mTW.mCurrentIndex < mTW.mPlaylistRecord.mCLength) {
    		mTW.mCurrentAPath = mTW.mPlaylistRecord.mLName[mTW.mCurrentIndex].mPath;
        	if(prepare(mTW.mCurrentAPath) == 0) {
        		start();
        		seekTo(pos);
        		save();
        		return true;
        	}
    	}
    	return false;
    }

    private void notifyChange() {
    	int percent = 0;
		int duration = getDuration();
		int position = getCurrentPosition();
		if(duration < 0) {
			duration = 0;
		}
		if(position < 0) {
			position = 0;
		}
    	if((duration > 0) && (position <= duration)) {
    		percent = position * 100 / duration;
    	}
        String titleName = getTrackName();
        if(titleName == null) {
        	titleName = getFileName();
        	if(titleName == null) {
        		titleName = getString(R.string.unknown);
        	}
        }
        mTW.write(0x9f00, 0x03, (isPlaying() ? 0x80 : 0x00) | (percent & 0x7f), titleName);
        mTW.write(0x0303, 0x03, (isPlaying() ? 0x80 : 0x00) | (percent & 0x7f), titleName);
		if(mAHandler != null) {
			mAHandler.sendEmptyMessage(NOTIFY_CHANGE);
		}
    }

	public void current(int pos, boolean r) {
		synchronized (mTW) {
			int [] p = mTW.mRPlaylist;
			if(p != null) {
				int length = p.length;
				if(length > 0) {
					int index = mTW.mCurrentRIndex;
					if(r) {
						if(index < -1) {
							index = -1;
						}
						int i;
						for(i = index; i > -1; i--) {
							mTW.mCurrentIndex = p[i];
			    			if(play(pos)) {
			    				mTW.mCurrentRIndex = i;
		        				pos = 0;
			    				break;
			    			}
			    			pos = 0;
						}
						if((mTW.mRepeat != 0) && (i == -1)) {
							for(i = length - 1; i > index; i--) {
								mTW.mCurrentIndex = p[i];
			        			if(play(pos)) {
			        				mTW.mCurrentRIndex = i;
			        				pos = 0;
			        				break;
			        			}
			        			pos = 0;
							}
							if(i == index) {
			    				stop();
							}
						}
						if(mTW.mCurrentRIndex == -1) {
							mTW.mCurrentRIndex = 0;
							mTW.mCurrentIndex = p[mTW.mCurrentRIndex];
				    		stop();
						}
					} else {
						if(index > length) {
							index = length;
						}
						int i;
						for(i = index; i < length; i++) {
							mTW.mCurrentIndex = p[i];
			    			if(play(pos)) {
			    				mTW.mCurrentRIndex = i;
		        				pos = 0;
			    				break;
			    			}
			    			pos = 0;
						}
			    		if((mTW.mRepeat != 0) && (i == length)) {
			    			for(i = 0; i < index; i++) {
			    				mTW.mCurrentIndex = p[i];
			        			if(play(pos)) {
			        				mTW.mCurrentRIndex = i;
			        				pos = 0;
			        				break;
			        			}
			        			pos = 0;
			    			}
			    			if(i == index) {
			    				stop();
			    			}
			    		}
						if(mTW.mCurrentRIndex == length) {
							mTW.mCurrentRIndex = length - 1;
							mTW.mCurrentIndex = p[mTW.mCurrentRIndex];
				    		stop();
						}
					}
				}
			}
		}
	}

    private boolean hasMessagesNP() {
    	return (mHandler.hasMessages(NEXT) || mHandler.hasMessages(PREV));
    }

    private void _next() {
    	if((mTW.mRPlaylist != null) && (mTW.mRPlaylist.length > 0)) {
    		if(mTW.mRepeat != 2) {
    			mTW.mCurrentRIndex++;
    		}
        	current(0, false);
    	}
    }
    
    public void next() {
    	if (isError) {
    		isError = false;
			return;
		}
    	if(!hasMessagesNP()) {
    		mHandler.sendEmptyMessage(NEXT);
    	}
    }

    private void _prev() {
    	if((mTW.mRPlaylist != null) && (mTW.mRPlaylist.length > 0)) {
    		if(mTW.mRepeat != 2) {
    			mTW.mCurrentRIndex--;
    		}
        	current(0, true);
    	}
    }
    
    public void prev() {
    	if(!hasMessagesNP()) {
    		mHandler.sendEmptyMessage(PREV);
    	}
    }

    private void save() {
    	mTW.mCurrentPos = mMediaPlayer.getCurrentPosition();
		try {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter("/data/tw/music"));
				bw.write(mTW.mCurrentAPath);
				bw.write('\n');
				bw.write(Integer.toString(mTW.mCurrentIndex));
				bw.write('\n');
				bw.write(Integer.toString(mTW.mCurrentPos));
				bw.write('\n');
				bw.write(Integer.toString(mTW.mShuffle));
				bw.write('\n');
				bw.write(Integer.toString(mTW.mRepeat));
				bw.write('\n');
				bw.flush();
			} catch (Exception e) {
				new File("/data/tw/music").delete();
			} finally {
				if(bw != null) {
					bw.close();
					bw = null;
				}
			}
			FileUtils.setPermissions("/data/tw/music", 0666, -1, -1);
		} catch (Exception e) {
		}
    }

    public void setAHandler(Handler handler) {
    	mAHandler = handler;
    }

    public int getDuration() {
    	return mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
    	return mMediaPlayer.getCurrentPosition();
    }

    public String getArtistName() {
    	return mTW.mCurrentArtist;
    }

    public String getAlbumName() {
    	return mTW.mCurrentAlbum;
    }

    public String getTrackName() {
    	return mTW.mCurrentSong;
    }
    
    public Bitmap getAlbumArt() {
    	return mTW.mAlbumArt;
    }

    public String getFileName() {
    	if (mTW.mCurrentIndex < mTW.mPlaylistRecord.mCLength) {
    		return mTW.mPlaylistRecord.mLName[mTW.mCurrentIndex].mName;
    	}
    	return null;
    }
}
