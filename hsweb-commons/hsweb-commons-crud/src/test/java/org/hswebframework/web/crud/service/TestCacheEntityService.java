package org.hswebframework.web.crud.service;

import org.hswebframework.web.crud.entity.TestEntity;
import org.springframework.stereotype.Service;

@Service
public class TestCacheEntityService extends GenericReactiveCacheSupportCrudService<TestEntity,String> {

}
