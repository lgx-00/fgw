package com.pxxy.enums;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.pxxy.pojo.Project;

import java.util.Date;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-07-07-21:03
 * @Description:
 */
public enum ProjectStageEnum {

    ALL {
        public List<Project> list(QueryChainWrapper<Project> wrapper) {
            return wrapper.list();
        }
    },

    NOT_STARTED {
        public List<Project> list(QueryChainWrapper<Project> wrapper) {
            Date now = new Date();
            return wrapper.gt("IFNULL(pro_dis_start,'2222-12-22')", now).list();
        }
    },

    NOT_COMPLETED {
        public List<Project> list(QueryChainWrapper<Project> wrapper) {
            Date now = new Date();
            return wrapper.le("pro_dis_start", now)
                    .gt("ifnull(pro_dis_complete,'2222-12-22')", now).list();
        }
    },

    COMPLETED {
        public List<Project> list(QueryChainWrapper<Project> wrapper) {
            Date now = new Date();
            return wrapper.le("ifnull(pro_dis_complete,'2222-12-22')", now).list();
        }
    };

    public abstract List<Project> list(QueryChainWrapper<Project> wrapper);
}
