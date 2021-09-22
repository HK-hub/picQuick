package com.cqut.picquick;

/**
 * @author : HK意境
 * @ClassName : ElasticSearchTest
 * @date : 2021/9/17 17:10
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    // 1.测试索引的创建
    @Test
    void testCreateIndex() throws IOException {
        // 1.创建索引
        CreateIndexRequest createIndexRequest=new CreateIndexRequest("wangjiulong_index");
        // 2.客户端执行请求
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        // 3.打印索引信息
        System.out.println(createIndexResponse);
    }
}
