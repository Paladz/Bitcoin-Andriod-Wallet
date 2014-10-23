package com.colt.mwallet;

import java.io.File;

import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.InsufficientMoneyException;
import com.google.zxing.WriterException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SentActivity extends Activity {

	
	Colt CoinSystem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sent);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		
		
		setCoinSystem();
		setQrhint();	
		
	}
	
	public void setCoinSystem()
	{
		
		if(Colt.type.equals("Bitcoin"))
    	{
			if(ColtSHA256.GettheOne() == null)
			{
				File blockFile = new File(this.getFilesDir(), "BlockStore");
	        	File walletFile = new File(this.getFilesDir(), "BitcoinWallet");
	        	try {
					ColtSHA256 CoinSystem = new ColtSHA256(walletFile, blockFile);
				} catch (Exception e) {}	
			}
			CoinSystem = ColtSHA256.GettheOne();
    	}
		
		if(Colt.type.equals("Litecoin"))
    	{
			if(ColtScrypt.GettheOne() == null)
			{
				File blockFile = new File(this.getFilesDir(), "BlockStore");
	        	File walletFile = new File(this.getFilesDir(), "BitcoinWallet");
	        	try {
					ColtScrypt CoinSystem = new ColtScrypt(walletFile, blockFile);
				} catch (Exception e) {}	
			}
			CoinSystem = ColtScrypt.GettheOne();
    	}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sent, menu);
		return true;
	}
	
	public void ActivitySelected(View view) {
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivity(intent);
    }
	
	public void TransactionSelected(View view) {
    }
	
	public void WalletSelected(View view) {
		Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);
    }
	
	
	public void SetSelected(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
	
	public void QRSelected(View view) throws WriterException {
        CoinSystem.QR = !CoinSystem.QR;
        setQrhint();
    }
	
	public void addressclicked(View view)
	{
		if(CoinSystem.QR)
		{	 
			 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		     intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		     intent.putExtra("SAVE_HISTORY", false);
		     startActivityForResult(intent, 0);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            	EditText address = (EditText)findViewById(R.id.address);
            	address.setText(data.getStringExtra("SCAN_RESULT"));
            }
        }
    }
	
	public void setQrhint()
	{
		ImageView img= (ImageView) findViewById(R.id.qr);
        if(CoinSystem.QR)
        {
        	img.setImageResource(R.drawable.ic_qron);
        }
        else
        {
        	img.setImageResource(R.drawable.ic_qroff);
        }	
	}
	
	public void PaySelected(View view) throws Exception {
		EditText address = (EditText)findViewById(R.id.address);
		EditText value = (EditText)findViewById(R.id.amount);

		long sentVal = (long) (Double.parseDouble(value.getText().toString()) * 100000000);
		String addreVal = address.getText().toString();
		CoinSystem.SentCoin(addreVal, sentVal);
		
		address.setText("");
		address.setText("");
		
		Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);	
    }
	

	


}
