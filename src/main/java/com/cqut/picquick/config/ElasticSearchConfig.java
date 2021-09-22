package com.cqut.picquick.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author : HK意境
 * @ClassName : ElasticSearchConfig
 * @date : 2021/9/17 17:01
 * @description :关于elasticSearch的客户端配置
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
//@Configuration
public class ElasticSearchConfig {

    //@Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1",9200,"http")
                )
        );

        return restHighLevelClient ;
    }

}
