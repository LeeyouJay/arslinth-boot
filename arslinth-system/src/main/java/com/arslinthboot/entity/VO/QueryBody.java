package com.arslinthboot.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Arslinth
 * @ClassName QueryBody
 * @Description TODO
 * @Date 2021/3/5
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryBody {

    private boolean onMark;

    private String roleId;

    private String code;

    private String codeType;

    private String state;

    private String searchName;

    private long pageIndex;

    private long pageSize;
}
