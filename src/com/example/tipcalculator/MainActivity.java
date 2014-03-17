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

public class MainActivity extends Activity {
	
	RadioGroup rg_1;
	RadioGroup rg_2;
	Boolean checkListenerSuppressed = false;
	EditText et_tip_other;
	RadioButton rb_10;
	RadioButton rb_15;
	RadioButton rb_20;
	RadioButton rb_other;
	EditText et_bill_amount;
	TextView tv_tip_value;
	TextView tv_total_value;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		
		rg_1 =  (RadioGroup) findViewById(R.id.rg_1);
		rg_2 =  (RadioGroup) findViewById(R.id.rg_2);
		et_tip_other = (EditText) findViewById(R.id.et_tip_other);
		rb_10 = (RadioButton) findViewById(R.id.rb_10);
		rb_15 = (RadioButton) findViewById(R.id.rb_15);
		rb_20 = (RadioButton) findViewById(R.id.rb_20);
		rb_other = (RadioButton) findViewById(R.id.rb_other);
		et_bill_amount = (EditText) findViewById(R.id.et_bill_amount);
		tv_tip_value = (TextView) findViewById(R.id.tv_tip_value);
		tv_total_value = (TextView) findViewById(R.id.tv_total_value);

		rg_1.clearCheck();
		rg_2.clearCheck();
		
		rg_1.setOnCheckedChangeListener(checkListener);
		rg_2.setOnCheckedChangeListener(checkListener);
		et_tip_other.setOnFocusChangeListener(focusListener);
		et_tip_other.addTextChangedListener(textListener);
		et_bill_amount.addTextChangedListener(textListener);
		
		calculateTip();
	}

	private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if ( checkListenerSuppressed ) {
				return;
			}
			checkListenerSuppressed = true;
			if ( group == rg_1 ) {
				rg_2.clearCheck();
			} else if ( group == rg_2 ) {
				rg_1.clearCheck();
			}
			if ( checkedId != -1 ) {
				if ( checkedId == rb_other.getId() ) {
					et_tip_other.requestFocus();
				} else {
					et_tip_other.clearFocus();
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
			rg_1.clearCheck();
			rb_other.setChecked(true);
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
		
		tv_tip_value.setText( NumberFormat.getCurrencyInstance().format(tipAmount).replaceAll("\\.00", "") );
		tv_total_value.setText( NumberFormat.getCurrencyInstance().format(tipAmount + getBillAmount()).replaceAll("\\.00", "") );
	}
	
	private float getTip() {
		if ( rb_10.isChecked() ) {
			return 10;
		}
		if ( rb_15.isChecked() ) {
			return 15;
		}
		if ( rb_20.isChecked() ) {
			return 20;
		}
		if ( rb_other.isChecked() ) {
			try {
				return Float.valueOf(et_tip_other.getText().toString());
			} catch (Exception e) { }
		}
		return 0;
	}
	
	private float getBillAmount() {
		try {
			return Float.valueOf(et_bill_amount.getText().toString());
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
