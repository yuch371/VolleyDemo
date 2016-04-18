package demo.cn.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView mTextView;
    Button mButton1,mButton2,mButton3;
    ImageView mImageView1,mImageView2;
    RequestQueue mRequestQueue;
    RequestQueue mRequestQueueOKhttp;
    //API23和V4都有
    LruCache <String ,Bitmap> mBitmapLruCache;
    com.android.volley.toolbox.NetworkImageView mNetworkImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFind();
        initData();
    }

    private void initData() {
        //第1步，实例化一个请求队列
        mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        mNetworkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setErrorImageResId(R.mipmap.ic_launcher);

        mBitmapLruCache=new LruCache<String ,Bitmap>((int) (Runtime.getRuntime().freeMemory()/8)){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
                //return super.sizeOf(key, value);
            }
        };
    }

    private void  initFind() {
        mTextView= (TextView) findViewById(R.id.textView1);
        mButton1= (Button) findViewById(R.id.button1);
        mButton2= (Button) findViewById(R.id.button2);
        mButton3= (Button) findViewById(R.id.button3);
        mImageView1= (ImageView) findViewById(R.id.iamageView1);
        mImageView2= (ImageView) findViewById(R.id.iamageView2);
        mNetworkImageView= (NetworkImageView) findViewById(R.id.iamageView3);
    }

    public void buttonClick(View v){
        int id=v.getId();
        switch (id){
            case R.id.button1 :
                doStringRequest();
                break;
            case R.id.button2 :
                doJsonRequest();
                break;
            case R.id.button3 :
                doJsonArrayRequest();
                break;
            case R.id.button4 :
                doImageRequest();
                break;
            case R.id.button5 :
                doImageLoader();
                break;
            case R.id.button6 :
                doNetImageView();
                break;
        }
    }

    private void doNetImageView() {
        // mNetworkImageView.setImageResource(R.mipmap.ic_launcher);

        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return mBitmapLruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                mBitmapLruCache.put(s,bitmap);
            }
        });
        mNetworkImageView.setImageUrl("http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg",
                imageLoader);
    }


    private void doJsonRequest() {
        String url="http://www.weather.com.cn/adat/sk/101010100.html";
        //String url="http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg";

        //第2步 生成一个XXX请求
        //Method.POST
        Map<String,String> map=new HashMap<String,String>();
        JSONObject params = new JSONObject(map);

        //StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,params, new Response.Listener<JSONObject>() {
        //实际测试时不稳定
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(MainActivity.this, "下载的内容为:" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "下载内容异常:"+volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        mRequestQueue.add(jsonObjectRequest);
    }
    private void doJsonArrayRequest() {
        String url="http://www.weather.com.cn/adat/sk/101010100.html";
        //String url="http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg";

        //第2步 生成一个XXX请求
        //Method.POST
        //StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url,new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray jsonArray) {
                Toast.makeText(MainActivity.this, "下载的内容为:" + jsonArray.toString(), Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "下载内容异常:"+volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(jsonArrayRequest);
    }

    private void doStringRequest() {
        String url="http://www.baidu.com";
        //String url="http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg";

        //第2步 生成一个XXX请求
        //Method.POST
        //StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("StringRequest", s);
                Toast.makeText(MainActivity.this, "下载的内容为:" + s, Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("StringRequest",volleyError.getMessage());
                Toast.makeText(MainActivity.this, "下载内容异常:"+volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //第3步 把生成的XXX请求，添加到请求队列中，由后台自动处理
        mRequestQueue.add(stringRequest);
    }

    private void doImageRequest() {
        String url="http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg";
        mImageView1.setImageResource(R.mipmap.ic_launcher);
        ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d("onResponse","bitmap.getByteCount()"+bitmap.getByteCount());
                mImageView1.setImageBitmap(bitmap);
            }
        },0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse","error"+error.getMessage());
                mImageView1.setImageResource(R.mipmap.ic_launcher);
            }
        } );
        //第3步 把生成的XXX请求，添加到请求队列中，由后台自动处理
        mRequestQueue.add(imageRequest);
    }

    private void doImageLoader() {
        //1. 创建一个RequestQueue对象。
        //2. 创建一个ImageLoader对象。
        //3. 获取一个ImageListener对象。
        //4. 调用ImageLoader的get()方法加载网络上的图片。
        //第2步
        //第2.1步  利用请求队列和图片缓存生成一个ImageLoader的实例
        //第2.2步 ,准备好一个图片的内存缓存，并实现图片的缓存
        ImageLoader imageLoader=new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return mBitmapLruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                mBitmapLruCache.put(s,bitmap);
            }
        });
        //第3步获取一个ImageListener对象
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView2,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //第4步 调用ImageLoader的get()方法加载网络上的图片。
        String url="http://www.pingwest.com/wp-content/uploads/2013/04/new_android_wallpaper.jpg";
        imageLoader.get(url, listener);
    }
}
