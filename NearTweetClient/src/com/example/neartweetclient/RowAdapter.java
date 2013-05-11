package com.example.neartweetclient;

import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RowAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private final String[] values;
	
	  public RowAdapter(Context context, String[] values) {
		    super(context, R.layout.simplerow, values);
		    this.context = context;
		    this.values = values;
		  }
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.simplerow, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.rowTextView);
	  //  ImageView imageView = (ImageView) rowView.findViewById(R.id.imagem);
	    
	    // Change the icon for Windows and iPhone
	    String s = values[position];
	    if (s.contains("@image")) {
	    	String[] str=s.split("@image-");
	    	textView.setText(str[0]);
	    	byte[] bytes=str[1].getBytes();
	    		Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	  
	     // imageView.setImageBitmap(image);
	    } else {
	    	
	    	textView.setText(values[position]);
	      
	    }

	    return rowView;
	  }
}
