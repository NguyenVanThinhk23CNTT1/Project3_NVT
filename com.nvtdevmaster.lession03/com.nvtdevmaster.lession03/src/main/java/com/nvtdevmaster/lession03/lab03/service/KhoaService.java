package com.nvtdevmaster.lession03.lab03.service;

import com.nvtdevmaster.lession03.lab03.entity.Khoa;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class KhoaService {
    private final List<Khoa> list = new CopyOnWriteArrayList<>(List.of(
            new Khoa("CNTT","Công nghệ thông tin"),
            new Khoa("QTKD","Quản trị kinh doanh"),
            new Khoa("DTVT","Điện tử viễn thông"),
            new Khoa("CK","Cơ khí"),
            new Khoa("XD","Xây dựng")
    ));

    public List<Khoa> findAll(){ return list; }
    public Khoa findById(String makh){
        return list.stream().filter(k->k.getMakh().equalsIgnoreCase(makh))
                .findFirst().orElse(null);
    }
    public Khoa create(Khoa k){
        if(findById(k.getMakh())!=null) throw new IllegalArgumentException("Mã khoa đã tồn tại");
        list.add(k); return k;
    }
    public Khoa update(String makh, Khoa input){
        Khoa k = findById(makh);
        if(k==null) return null;
        k.setTenkh(input.getTenkh());
        return k;
    }
    public boolean delete(String makh){
        Khoa k = findById(makh);
        return k!=null && list.remove(k);
    }
}
