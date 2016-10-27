package com.smartcity.network;

import com.smartcity.util.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONHandler {

    static String result = null;

    public String makeServiceCall( String urlString,ArrayList<NameValuePair> value)
    {
        try
        {

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            HttpPost httpPost = new HttpPost(urlString);

            httpPost.setEntity(new UrlEncodedFormEntity(value));
            HttpResponse response = httpClient.execute(httpPost);
            result = inputStreamToString(response.getEntity().getContent()).toString();


        }
        catch (UnsupportedEncodingException e)
        {

        }
        catch (ClientProtocolException e)
        {

        }
        catch (MalformedURLException e)
        {

        }
        catch (IOException e)
        {

        }
        return result;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            e.printStackTrace();

        }
        return answer;
    }

    public String makeServiceCallUserProfilePictureUpdate(String url, List<NameValuePair> urlParams) {

        String response=null;

        try
        {
            @SuppressWarnings( "deprecation" )
            MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );

            for( int index = 0; index < urlParams.size(); index++ )
            {
                System.out.println("jsonPostImage: " + urlParams.get( index ).getName());

                if( urlParams.get( index ).getName().equalsIgnoreCase(Constants.file) )
                {
                    // If the key equals to image key name, we use FileBody to transfer
                    // the data
                    entity.addPart( urlParams.get( index ).getName(),
                            new FileBody( new File( urlParams.get( index ).getValue() ) ) );
                }
               /* else if( urlParams.get( index ).getName().equalsIgnoreCase(Constants.audio) )
                {
                    // If the key equals to image key name, we use FileBody to transfer
                    // the data
                    if(urlParams.get( index ).getName().equalsIgnoreCase("")){

                    }else {
                        entity.addPart(urlParams.get(index).getName(),
                                new FileBody(new File(urlParams.get(index).getValue())));
                    }
                }*/
                else
                {
                    // Normal string data
                    entity.addPart( urlParams.get( index ).getName(), new StringBody( urlParams.get( index ).getValue() ) );
                }
            }

//            System.out.println("jsonPostImage: " + entity.getContent());

            HttpURLConnection httpURLConnection = null;

            URL postCommentURL = new URL( url );

            httpURLConnection = (HttpURLConnection) postCommentURL.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(60000);

            httpURLConnection.addRequestProperty("Content-length", entity.getContentLength() + "");
//            httpURLConnection.addRequestProperty("Content-Type", "multipart/form-data");
            httpURLConnection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

//              Make Post Request
            OutputStream os = httpURLConnection.getOutputStream();
            entity.writeTo(httpURLConnection.getOutputStream());
            os.close();
            httpURLConnection.connect();

            int response_code = httpURLConnection.getResponseCode();

            if (HttpURLConnection.HTTP_OK == response_code) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {

                    builder = builder.append(line);
                }

                bufferedReader.close();
                response = builder.toString();

            }

        } catch( Exception e )
        {
            System.out.println("jsonPostImage: "+e);
        }

        return response;
    }
}