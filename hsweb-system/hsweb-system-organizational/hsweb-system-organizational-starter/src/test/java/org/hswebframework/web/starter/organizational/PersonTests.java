/*
 *  Copyright 2020 http://www.hswebframework.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.hswebframework.web.starter.organizational;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.hswebframework.web.entity.organizational.PersonEntity;
import org.hswebframework.web.starter.convert.FastJsonGenericHttpMessageConverter;
import org.hswebframework.web.tests.SimpleWebApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

/**
 * TODO 完善单元测试
 *
 * @author hsweb-generator-online
 */
public class PersonTests extends SimpleWebApplicationTests {

    @Autowired
    private FastJsonGenericHttpMessageConverter fastJsonHttpMessageConverter;

    @Test
    public void testCrud() throws Exception {
        PersonEntity entity = entityFactory.newInstance(PersonEntity.class);
        //todo 设置测试属性
        entity.setName("test");

        // test add data
        String requestBody = JSON.toJSONString(entity);
        JSONObject result = testPost("/person").setUp(setup -> setup.contentType(MediaType.APPLICATION_JSON).content(requestBody)).exec().resultAsJson();
        Assert.assertEquals(200, result.get("status"));
        String id = result.getString("result");
        Assert.assertNotNull(id);
        entity.setId(id);
        // test get data
        result = testGet("/person/" + id).exec().resultAsJson();
        entity = result.getObject("result", entityFactory.getInstanceType(PersonEntity.class));

        Assert.assertEquals(200, result.get("status"));
        Assert.assertNotNull(result.getJSONObject("result"));

        Assert.assertEquals(fastJsonHttpMessageConverter.converter(entity),
                fastJsonHttpMessageConverter.converter(result.getObject("result", entityFactory.getInstanceType(PersonEntity.class))));
        //todo 修改测试属性
        PersonEntity newEntity = entityFactory.newInstance(PersonEntity.class);
        newEntity.setName("test2");

        result = testPut("/person/" + id)
                .setUp(setup ->
                        setup.contentType(MediaType.APPLICATION_JSON)
                                .content(JSON.toJSONString(newEntity)))
                .exec().resultAsJson();
        Assert.assertEquals(200, result.get("status"));

        result = testGet("/person/" + id).exec().resultAsJson();
        result = result.getJSONObject("result");
        Assert.assertNotNull(result);

        result = testDelete("/person/" + id).exec().resultAsJson();
        Assert.assertEquals(200, result.get("status"));

        result = testGet("/person/" + id).exec().resultAsJson();
        Assert.assertEquals(404, result.get("status"));
    }
}
