package com.tbo.paapi;


import java.util.*;

import androidx.annotation.NonNull;

import android.os.StrictMode;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import com.amazon.paapi5.v1.ApiClient;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.Item;
import com.amazon.paapi5.v1.api.DefaultApi;
import com.amazon.paapi5.v1.SearchItemsResource;
import com.amazon.paapi5.v1.SearchItemsRequest;
import com.amazon.paapi5.v1.PartnerType;
import com.amazon.paapi5.v1.SearchItemsResponse;


/** PaapiPlugin */
public class PaapiPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "paapi");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("paapiInitiate")){
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

      String access_key = call.argument("accessKey");
      String secret_key = call.argument("secretKey");
      String partnerTag = call.argument("partnerTag");
      String host = call.argument("host");
      String region = call.argument("region");
      String keywords = call.argument("keyword");
      String searchIndex = call.argument("searchIndex");
      Integer itemPage = call.argument("itemPage");
      Integer itemCount = call.argument("itemCount");

      ApiClient client = new ApiClient();
      client.setAccessKey(access_key);
      client.setSecretKey(secret_key);
      client.setHost(host);
      client.setRegion(region);

      DefaultApi api = new DefaultApi(client);

      List<SearchItemsResource> searchItemsResources = new ArrayList<SearchItemsResource>();
      searchItemsResources.add(SearchItemsResource.ITEMINFO_TITLE);
      searchItemsResources.add(SearchItemsResource.OFFERS_LISTINGS_PRICE);
      searchItemsResources.add(SearchItemsResource.ITEMINFO_CLASSIFICATIONS);
      searchItemsResources.add(SearchItemsResource.IMAGES_PRIMARY_MEDIUM);
      searchItemsResources.add(SearchItemsResource.OFFERS_LISTINGS_SAVINGBASIS);

      SearchItemsRequest searchItemsRequest = new SearchItemsRequest().partnerTag(partnerTag).keywords(keywords)
              .searchIndex(searchIndex).resources(searchItemsResources).partnerType(PartnerType.ASSOCIATES).itemCount(itemCount).itemPage(itemPage);
      try {
        SearchItemsResponse response = api.searchItems(searchItemsRequest);
        HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
        if(response.getSearchResult() != null){
          List<Item> items = response.getSearchResult().getItems();
          System.out.println("ITEM size: " + items.size() + ", size in response: " +  response.getSearchResult().getTotalResultCount());

          for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            HashMap<String, String> itemMap =  new HashMap<String,String>();
            itemMap.put("price", item.getOffers().getListings().get(0).getPrice().getDisplayAmount());
            itemMap.put("url", item.getDetailPageURL());
            itemMap.put("imageUrl", item.getImages().getPrimary().getMedium().getURL());
            itemMap.put("title",item.getItemInfo().getTitle().getDisplayValue());
            map.put(String.valueOf(i), itemMap);
          }
        }
        result.success(map);
      } catch (ApiException exception){
        result.error("paapi api error", exception.getMessage(), null);
      } catch (Exception exception){
        result.error("paapi error", exception.getMessage(), null);
      }
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
