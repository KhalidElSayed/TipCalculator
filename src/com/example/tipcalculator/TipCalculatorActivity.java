package com.example.tipcalculator;

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TipCalculatorActivity extends Activity {
	
	RadioGroup rg1;
	RadioGroup rg2;
	EditText etTipOther;
	RadioButton rb10;
	RadioButton rb15;
	RadioButton rb20;
	RadioButton rbOther;
	EditText etBillAmount;
	TextView tvTipValue;
	TextView tvTotalValue;
	Boolean checkListenerSuppressed = false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_tip_calculator);
		
		rg1 =  (RadioGroup) findViewById(R.id.rg1);
		rg2 =  (RadioGroup) findViewById(R.id.rg2);
		etTipOther = (EditText) findViewById(R.id.etTipOther);
		rb10 = (RadioButton) findViewById(R.id.rb10);
		rb15 = (RadioButton) findViewById(R.id.rb15);
		rb20 = (RadioButton) findViewById(R.id.rb20);
		rbOther = (RadioButton) findViewById(R.id.rbOther);
		etBillAmount = (EditText) findViewById(R.id.etBillAmount);
		tvTipValue = (TextView) findViewById(R.id.tvTipValue);
		tvTotalValue = (TextView) findViewById(R.id.tvTotalValue);

		rg1.clearCheck();
		rg2.clearCheck();
		
		rg1.setOnCheckedChangeListener(checkListener);
		rg2.setOnCheckedChangeListener(checkListener);
		etTipOther.setOnFocusChangeListener(focusListener);
		etTipOther.addTextChangedListener(textListener);
		etBillAmount.addTextChangedListener(textListener);
		
		calculateTip();
	}

	private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if ( checkListenerSuppressed ) {
				return;
			}
			checkListenerSuppressed = true;
			if ( group == rg1 ) {
				rg2.clearCheck();
			} else if ( group == rg2 ) {
				rg1.clearCheck();
			}
			if ( checkedId != -1 ) {
				if ( checkedId == rbOther.getId() ) {
					etTipOther.requestFocus();
				} else {
					etTipOther.clearFocus();
				}
			}
			checkListenerSuppressed = false;
			calculateTip();
		}
		
	};
	
	private OnFocusChangeListener focusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if ( checkListenerSuppressed ) {
				return;
			}
			checkListenerSuppressed = true;
			rg1.clearCheck();
			rbOther.setChecked(true);
			checkListenerSuppressed = false;
			calculateTip();
		}
		
	};
	
	private TextWatcher textListener = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			calculateTip();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) { }
		
	};
	
	private void calculateTip() {
		Float tipAmount = getBillAmount() * getTip() / 100;
		
		tvTipValue.setText( NumberFormat.getCurrencyInstance().format(tipAmount).replaceAll("\\.00", "") );
		tvTotalValue.setText( NumberFormat.getCurrencyInstance().format(tipAmount + getBillAmount()).replaceAll("\\.00", "") );
	}
	
	private float getTip() {
		if ( rb10.isChecked() ) {
			return 10;
		}
		if ( rb15.isChecked() ) {
			return 15;
		}
		if ( rb20.isChecked() ) {
			return 20;
		}
		if ( rbOther.isChecked() ) {
			try {
				return Float.valueOf(etTipOther.getText().toString());
			} catch (Exception e) { }
		}
		return 0;
	}
	
	private float getBillAmount() {
		try {
			return Float.valueOf(etBillAmount.getText().toString());
		} catch (Exception e) {
			return 0;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
