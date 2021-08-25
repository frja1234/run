package com.scy.running.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MyPage<T> extends Page<T> {

    private static final long serialVersionUID = -1495574764016848960L;

    // 设置默认的分页，每页记录为5
    public MyPage(long current){super(current, 10);}

    public MyPage(long current, long size) {
        super(current, size);
    }
}
