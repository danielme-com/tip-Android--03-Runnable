package com.danielme.tipsandroid.runnable;

import java.lang.ref.WeakReference;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.danieme.tipsandroid.runnable.R;

public class MainActivity extends Activity
{
	ProgressBar progressBar;
	ImageView imageView;
	MyHandler myHandler = new MyHandler(MainActivity.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressBar = (ProgressBar) findViewById(R.id.progresss);
		imageView = (ImageView) findViewById(R.id.imageView1);
		progressBar.setVisibility(View.VISIBLE);

		new Thread()
		{
			public void run()
			{
				Message message = new Message();
				try
				{
					//espera de 3 segundos para que de tiempo a ver el progress
					Thread.sleep(3000);
			    	URL url = new URL("http://danielmedotcom.files.wordpress.com/2012/02/icon-danielme.png");
			    	Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			    	
					message.obj = bmp;
					message.what = 1;				
				}
				catch (Exception ex)
				{
					Log.e(MainActivity.class.toString(), ex.getMessage(), ex);
					message.what = 2;
					
				}
				finally
				{					
					myHandler.sendMessage(message);
				}
			}

		}.start();
	}
		
	//Solución estática
	private static class MyHandler extends Handler 
	{
	    private final WeakReference<MainActivity> myActivity;
	    
	    public MyHandler(MainActivity myActivity) 
	    {
	    	this.myActivity = new WeakReference<MainActivity>(myActivity);
	    }	
	    
	    @Override
		public void handleMessage(Message msg)
		{
			 //la tarea en segundo plano ya ha terminado. Ocultamos el progreso.
	    	myActivity.get().progressBar.setVisibility(View.GONE);
			 //si tenemos la imagen la mostramos
			 if (msg.what == 1 && msg.obj != null)
			 {
				 myActivity.get().imageView.setImageBitmap((Bitmap) msg.obj);
				 myActivity.get().imageView.setVisibility(View.VISIBLE);
			 }
			 //si no, informamos del error
			 else
			 {
				 	Builder builder = new Builder(myActivity.get());
					builder.setTitle(R.string.title);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setMessage(R.string.error);					
					builder.setNeutralButton(myActivity.get().getString(R.string.ok), null);			

					AlertDialog alertDialog = builder.create();
					alertDialog.show();
					alertDialog.setCancelable(false);
			 }
		}
	}
	
	//no estático
	/*private Handler getHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			 //la tarea en segundo plano ya ha terminado. Ocultamos el progreso.
			 progressBar.setVisibility(View.GONE);
			 //si tenemos la imagen la mostramos
			 if (msg.what == 1 && msg.obj != null)
			 {
				imageView.setImageBitmap((Bitmap) msg.obj);
				imageView.setVisibility(View.VISIBLE);
			 }
			 //si no, informamos del error
			 else
			 {
				 	Builder builder = new Builder(MainActivity.this);
					builder.setTitle(R.string.title);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setMessage(R.string.error);					
					builder.setNeutralButton(getString(R.string.ok), null);			

					AlertDialog alertDialog = builder.create();
					alertDialog.show();
					alertDialog.setCancelable(false);
			 }
			

		}

	};*/
	
}