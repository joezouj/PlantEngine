package com.sipai.tools;

import java.util.List;

public interface CommService<T> {

    public T selectById(String t);
    
    public int deleteById(String t);

    public int save(T t);
    
    public int update(T t);
    
    public List<T> selectListByWhere(String t);
    
    public int deleteByWhere(String t);
}
