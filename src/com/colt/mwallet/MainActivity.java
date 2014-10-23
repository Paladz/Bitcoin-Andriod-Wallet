package com.colt.mwallet;

import java.io.File;
import java.io.IOException;

import com.google.bitcoin.store.BlockStoreException;
import com.google.bitcoin.store.UnreadableWalletException;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void BitcoinSelected(View view) throws IOException, UnreadableWalletException, BlockStoreException  {
    	Colt.type = "Bitcoin";
    	if(ColtSHA256.GettheOne() == null)
    	{
    		File blockFile = new File(this.getFilesDir(), "BitcoinStore");
        	File walletFile = new File(this.getFilesDir(), "BitcoinWallet");
        	Colt CoinSystem = new ColtSHA256(walletFile, blockFile);   		
    	}
    	
        Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);
    }
    
    public void LitecoinSelected(View view) throws IOException, com.google.dogecoin.store.UnreadableWalletException, com.google.dogecoin.store.BlockStoreException {
    	Colt.type = "Litecoin";
    	if(ColtScrypt.GettheOne() == null)
    	{
    		File blockFile = new File(this.getFilesDir(), "LitecoinStore");
        	File walletFile = new File(this.getFilesDir(), "LitecoinWallet");
        	Colt CoinSystem = new ColtScrypt(walletFile, blockFile);   		
    	}
    	
        Intent intent = new Intent(this, CoinActivity.class);
        startActivity(intent);
    }
    
}
