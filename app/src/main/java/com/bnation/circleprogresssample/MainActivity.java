package com.bnation.circleprogresssample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bnation.circleprogress.CircleProgress;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		((Button)findViewById(R.id.animate_btn)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UIUtils.animateProgress((CircleProgress)findViewById(R.id.circleProgress),80.08f);
			}
		});
	}
}
