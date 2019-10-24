package com.justin.medical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Productlist extends AppCompatActivity implements TaskListner ,AdapterView.OnItemClickListener{

    public List<ProductPojo> feedItemList1 = new ArrayList<ProductPojo>();
    public List<Product> movieList = new ArrayList<Product>();
    private RecyclerView mRecyclerView;
    JsonRequester requester;
    String className;
    private ProductListAdapter mAdapter;
    String productListURL;
    String TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);


        requester = new JsonRequester(this);
        className = getLocalClassName();
        SharedPreferences prefs = getSharedPreferences("MEDICAL_SHARED_PREFS", MODE_PRIVATE);
        TOKEN = prefs.getString("TOKEN", null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Newproduct.class);
                startActivity(in);
            }
        });
        productListURL = "http://ec2-54-183-156-228.us-west-1.compute.amazonaws.com/b2cm-api/public/api/dispensary/product";
        getProductList();
    }

    public void getProductList() {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String>  paramsHeaders = new HashMap<String, String>();
        paramsHeaders.put("Authorization", TOKEN);
        requester.StringRequesterFormValuesWithHeadersOnly(productListURL, Request.Method.GET, className, "PRODUCTLIST", "PRODUCT_LIST_TAG",paramsHeaders );
    }

    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        try {
            if (cd == 00) {

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase("PRODUCTLIST")) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String names = jsonObject.getString("name");
                            String id = jsonObject.getString("id");
                            String desc = jsonObject.getString("description");
                            String pprice = jsonObject.getString("price");
                            String qty = jsonObject.getString("quatity");
                            String img = jsonObject.getString("image");
                            Product product = new Product();
                            product.setPname(names);
                            product.setPdesc(desc);
                            product.setImage(img);
                            product.setQuantity(qty);
                            product.setPrice(pprice);
                            product.setId(id);
                            movieList.add(product);
                        }
                        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
                        mAdapter = new ProductListAdapter(Productlist.this, movieList);
                        mRecyclerView.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {

        }
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("","MEDICAL: Clicked here at "+position);
    }
}
/*
 */