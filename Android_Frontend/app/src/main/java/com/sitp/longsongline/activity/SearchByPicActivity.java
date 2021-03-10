package com.sitp.longsongline.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.qmuiteam.qmui.widget.QMUIFontFitTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sitp.longsongline.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SearchByPicActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;

    public	static	final	int	CHOOSE_PHOTO	=	1;
    public	static	final	int	TAKE_PHOTO	=	2;
    private static final int UPLOADSUCCESS= 3;
    private static final int UPLOADFAIL= 4;
    private static final int GETPOEM=5;
    private Handler handler;
    private static String TAG="SearchByPicActivity";
    private  String uploadFileName;

    private	Uri	imageUri;

    private QMUIRoundButton uploadButton;
    private QMUIRoundButton searchButton;
    private QMUIFontFitTextView poemContentText;
    private TextView poemTitleText;
    private TextView poemAuthorText;

    private class Poem{
        public String title;
        public String content;
        public String author;
        Poem(String _title,String _content,String _author){
            title=_title;
            content=_content;
            author=_author;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_pic);
        //隐藏默认标题栏
        getSupportActionBar().hide();

        Init();
        InitImage();
    }
    private void Init(){
        //设置标题栏标题
        QMUITopBar topbar=(QMUITopBar)findViewById(R.id.topbar);
        topbar.setTitle("图片配诗词");

        poemContentText=(QMUIFontFitTextView)findViewById(R.id.poemContent);
        poemTitleText=(TextView)findViewById(R.id.poemTitle);
        poemAuthorText=(TextView)findViewById(R.id.poemAuthor);

        //处理线程间消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case  UPLOADSUCCESS:
                        uploadFileName=(String) msg.obj;
                        Toast.makeText(SearchByPicActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                        searchButton.setEnabled(true);
                        break;
                    case GETPOEM:
                        Poem poem=(Poem)msg.obj;
                        Toast.makeText(SearchByPicActivity.this,"匹配成功！",Toast.LENGTH_SHORT).show();
                        String[] lines=poem.content.split("\\|");
                        String poemContent=lines[0]+'，'+lines[1]+"。\n"+lines[2]+'，'+lines[3]+'。';
                        poemContentText.setText(poemContent);
                        poemTitleText.setText(poem.title);
                        poemAuthorText.setText(poem.author);
                        break;
                }
                return true;
            }
        });
    }
    private void InitImage(){
        findViewById(R.id.choose_from_album).setOnClickListener(this);
//        findViewById(R.id.take_photo).setOnClickListener(this);
        imageView = findViewById(R.id.picture);

        uploadButton=(QMUIRoundButton)findViewById(R.id.upload);
        searchButton=(QMUIRoundButton)findViewById(R.id.searchPoem);
        uploadButton.setOnClickListener(this);
        uploadButton.setEnabled(false);
        searchButton.setOnClickListener(this);
        searchButton.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.choose_from_album:
                chooseFromAlbum();
                break;
//            case R.id.take_photo:
//                takePhoto();
//                break;
            case R.id.upload:
                uploadPic();
                break;
            case R.id.searchPoem:
                searchPoem();
                break;

        }
    }

    //上传图片
    private void uploadPic(){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();
        File f= new File(this.getCacheDir(), "portrait");
        try {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(data);
            fos.flush();
            fos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        QMUIDialog dialog=new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("上传中...")
                .setCancelable(false)
                .show();

        //Post请求上传图片
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image","image.jpeg",RequestBody.create(MediaType.parse("image/jpeg"),f))
                .build();
        Request request = new Request.Builder()
                .url("http://120.25.145.41:6543/uploadImage")
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                if(response.code()==200){
                    try{
                        String responseBody=response.body().string();
                        JSONObject jsonobject=new JSONObject(responseBody);
                        String status=jsonobject.getString("status");
                        //成功上传图片
                        if(status.equals("true")){
                            String filename=jsonobject.getString("filename");
                            //给handler发送消息
                            Message msg = new Message();
                            msg.what = UPLOADSUCCESS;
                            msg.obj=filename;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (JSONException e){
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                }
                dialog.dismiss();

            }
        });

    }
    //匹配诗词
    private void searchPoem(){


        QMUIDialog dialog=new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("匹配中...")
                .setCancelable(false)
                .show();

        //Post请求上传图片
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image_path", uploadFileName)
                .build();
        Request request = new Request.Builder()
                .url("http://120.25.145.41:6543/searchPoemByImagePath")
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                if(response.code()==200){
                    try{
                        String responseBody=response.body().string();
                        JSONObject jsonobject=new JSONObject(responseBody);
                        String status=jsonobject.getString("status");
                        //成功上传图片
                        if(status.equals("true")){
                            JSONObject data=jsonobject.getJSONObject("data");
                            String description=data.getString("description");
                            JSONObject poem=data.getJSONObject("poem");
                            String content=poem.getString("content");
                            String title=poem.getString("title");
                            String author=poem.getString("author");
                            Poem resultPoem=new Poem(title,content,author);

                            //给handler发送消息
                            Message msg = new Message();
                            msg.what = GETPOEM;
                            msg.obj=resultPoem;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (JSONException e){
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                }
                dialog.dismiss();

            }
        });

    }

    private void takePhoto(){
        //	创建File对象，用于存储拍照后的图片
        File outputImage	=	new File(getExternalCacheDir(),
                "output_image.jpg");
        try	{
            if	(outputImage.exists())	{
                outputImage.delete();
            }
            outputImage.createNewFile();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        if	(Build.VERSION.SDK_INT	>=	24){
            imageUri=FileProvider.getUriForFile(SearchByPicActivity.this,
                    "com.sitp.longsongline.fileprovider",	outputImage);
        }
        else {
            imageUri=Uri.fromFile(outputImage);
        }
        //	启动相机程序
        Intent	intent	=	new	Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,	imageUri);
        startActivityForResult(intent,	TAKE_PHOTO);
    }

    private void chooseFromAlbum(){
        if((ContextCompat.checkSelfPermission(SearchByPicActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)	!=	PackageManager.
                PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(SearchByPicActivity.this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else{
            openAlbum();
        }
    }

    private	void openAlbum()	{
        Intent	intent	=	new	Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,	CHOOSE_PHOTO);//	打开相册
    }

    @Override
    public void onRequestPermissionsResult(int	requestCode,	String[]	permissions,
                                           int[]	grantResults)
    {
        switch	(requestCode) {
            case 1:
                if(grantResults.length	>	0	&&	grantResults[0]	==	PackageManager.
                        PERMISSION_GRANTED){
                    openAlbum();
                }
                else
                {
                    Toast.makeText(this,	"You	denied	the	permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int	requestCode,	int	resultCode,	Intent	data)	{
        super.onActivityResult(requestCode,resultCode,data);
        switch	(requestCode)	{
            case CHOOSE_PHOTO:
                handleImageOnKitKat(data);
                break;
            case TAKE_PHOTO:
                if	(resultCode	==	RESULT_OK)	{
                    try {
                        //	将拍摄的照片显示出来
                        Bitmap	bitmap	=	BitmapFactory.decodeStream(getContentResolver().
                                openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private	void	handleImageOnKitKat(Intent	data)	{
        String	imagePath	=	null;
        Uri uri	=	data.getData();
        if	(DocumentsContract.isDocumentUri(this,	uri)) {
            //	如果是document类型的Uri，则通过document	id处理
            String	docId	=	DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority()))	{
                String	id	=	docId.split(":")[1];	//	解析出数字格式的id
                String	selection	=	MediaStore.Images.Media._ID	+	"="	+	id;
                imagePath	=	getImagePath(MediaStore.Images.Media.
                                EXTERNAL_CONTENT_URI,	selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.
                    getAuthority()))
            {
                Uri	contentUri	=	ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            //	如果是content类型的Uri，则使用普通方式处理
            imagePath=getImagePath(uri,	null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme()))
        {
            //	如果是file类型的Uri，直接获取图片路径即可
            imagePath=uri.getPath();
        }
        displayImage(imagePath);	//	根据图片路径显示图片
    }

    private	String	getImagePath(Uri	uri,	String	selection){
        String	path	=	null;
        //	通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,	null,	selection,	null,	null);
        if	(cursor	!=	null){
            if	(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.
                        Images.Media.DATA));
                cursor.close();
            }
        }
        return path;
    }

    private	void displayImage(String imagePath){
        if	(imagePath	!=	null)	{
            Toast.makeText(this,	"success	to	get	image",	Toast.LENGTH_SHORT).show();
            Bitmap bitmap	=	BitmapFactory.decodeFile(imagePath);
            if(bitmap==null)
                Log.d("Bitmap", "null");
            imageView.setImageBitmap(bitmap);
            uploadButton.setEnabled(true);
            searchButton.setEnabled(false);
        }
        else{
            Toast.makeText(this,	"failed	to	get	image",	Toast.LENGTH_SHORT).show();
        }
    }
}