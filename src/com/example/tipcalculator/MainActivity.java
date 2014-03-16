package com.example.tipcalculator;

import java.math.BigDecimal;
import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	EditText bill_amount;
	TextView tip_amount;
	ToggleButton toggle_10;
	ToggleButton toggle_15;
	ToggleButton toggle_20;
	ToggleButton active_toggle;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bill_amount = (EditText) findViewById(R.id.bill_amount);	 
		tip_amount = (TextView) findViewById(R.id.tip_amount);	 
		toggle_10 = (ToggleButton) findViewById(R.id.toggle_10);
		toggle_15 = (ToggleButton) findViewById(R.id.toggle_15);
		toggle_20 = (ToggleButton) findViewById(R.id.toggle_20);

		toggle_10.setOnCheckedChangeListener(changeChecker);
		toggle_15.setOnCheckedChangeListener(changeChecker);
		toggle_20.setOnCheckedChangeListener(changeChecker);
		
		bill_amount.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				calculateTip();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
		
		setTipAmount(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void calculateTip() {
		if(active_toggle==null){
			setTipAmount(0);
			return;
		}

		float billAmount;
		try {
			billAmount = Float.valueOf(bill_amount.getText().toString());
		} catch (Exception e) {
			setTipAmount(0);
			return;
		}
		
		float tipPercentage = 0;
		if ( active_toggle == toggle_10 ) {
			tipPercentage = 10;			
		} else if ( active_toggle == toggle_15 ) {
			tipPercentage = 15;			
		} else if ( active_toggle == toggle_20 ) {
			tipPercentage = 20;			
		}
		
		setTipAmount(billAmount * tipPercentage / 100);
	}
	
	private void setTipAmount(float amount) {
		tip_amount.setText(
				getString(
						R.string.tip_is,
						NumberFormat.getCurrencyInstance().format(amount).replaceAll("\\.00", "")
				)
		);
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	OnCheckedChangeListener changeChecker = new OnCheckedChangeListener() {
		@Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if ( isChecked ) {
				if ( active_toggle != null ) {
					active_toggle.setChecked(false);
				}
				active_toggle = (ToggleButton) buttonView;
			} else {
				active_toggle = null;
			}
			calculateTip();
		}
	};

}
