package com.ksy.senfal;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.ksy.senfal.CalModel;
public class CalculalteAcclerationTask extends AsyncTask<float[], Void, CalModel>{
	
    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() 
     * You can specify the type of the parameters, the progress values, 
     * and the final value of the task, using generics
     * The method doInBackground() executes automatically on a worker thread
     * onPreExecute(), onPostExecute(), and onProgressUpdate() are all invoked on the UI thread
     * The value returned by doInBackground() is sent to onPostExecute()
     * You can call publishProgress() at anytime in doInBackground() 
     * to execute onProgressUpdate() on the UI thread
     * You can cancel the task at any time, from any thread*/
	@Override
	protected CalModel doInBackground(float[]... params) {
		// TODO Auto-generated method stub
		
		return new CalModel();
	}
   /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
   protected void onPostExecute(CalModel result) {
//       mImageView.setImageBitmap(result);

   }
}
