package com.fsapp.jl.voicerecognizer;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;


public class VoiceRecognizer extends Activity {
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		  if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK)   
	        {   
	            // 取得语音的字符   
	            matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);    
	        }   
	  
	        super.onActivityResult(requestCode, resultCode, data);   
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		matches = new ArrayList<String>();
		super.onCreate(savedInstanceState);
	}
	final int VOICE_RECOGNITION_REQUEST_CODE = 0xfe;
	List<String> matches = null;
	String prompt = null;
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public List<String> getMatches() {
		return matches;
	}
	boolean supportVoiceRecognize(){
		boolean ret;
		PackageManager pm = getPackageManager();   
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);   
        if (activities.size() != 0)   
        {   
            ret = true;
        }   
        else   
        {   
            ret = false;
        }   
		return ret;
	}
	boolean startRecognize(){
		 //通过Intent传递语音识别的模式   
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);   
        //语言模式和自由形式的语音识别   
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);   
        //提示语音开始   
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);   
        //开始执行我们的Intent、语音识别   
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE); 
		return false;		
	}	
}
