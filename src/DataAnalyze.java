import com.alibaba.fastjson.JSONObject;

public class DataAnalyze {
	
	
	//Get data from sql and Send data from Server to Client!
	public static String send(JSONObject info) {
		String data=null;
		
		//把数据库拿到的数据，作为一个一个JSONArray对象。然后生成JSONObject。然后作为String返回。
		
		
		
		
		return data;
	}
	
	
	//write data from client to sql
	public static String receive(JSONObject jsonObject) {
		String data=null;
		// 获取数据对象
				JSONObject user_data = jsonObject.getJSONObject("user_data");
				com.alibaba.fastjson.JSONArray normal = user_data.getJSONArray("normal");
				com.alibaba.fastjson.JSONArray jinricaijia = user_data.getJSONArray("caijia");
		
				// 遍历方式2
				for (Object obj : normal) {
					JSONObject goods_datas = (JSONObject) obj;
					Object goods_id = goods_datas.get("number");
					String goods_name = (String) goods_datas.get("name");
					String price = (String) goods_datas.get("price");
					System.out.format("goodsid:%s\t goodsname:%s\t price:%s\n", goods_id, goods_name, price);
				}
				for (Object jrcj:jinricaijia) {
					JSONObject caijia = (JSONObject) jrcj;
					String name = caijia.getString("name");
					String price = caijia.getString("price");
					String date = caijia.getString("date");
					System.out.format("name:%s\t price:%s\t date:%s\n", name,price,date);
					
				}
			//数据写入到数据库，采用mybatis管理数据库的连接与操作。
				
		data = "success";
		return data;
	}

}
