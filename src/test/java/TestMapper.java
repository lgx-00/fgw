import com.pxxy.FGWApplication;
import com.pxxy.entity.pojo.Project;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.mapper.ProjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hesen
 * @since 2023-06-29-13:30
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FGWApplication.class)
public class TestMapper {
    @Resource
    private ProjectMapper projectMapper;
    @Test
    public void test(){
        List<Project> allProjectByUser = projectMapper.getAllProjectByUser(2, null, 113);
        System.out.println(allProjectByUser);
        System.out.println(allProjectByUser.size());

        List<Project> projectList = projectMapper.getVagueProjectByUser(null);
        System.out.println(projectList);
        System.out.println(projectList.size());
    }
    @Test
    public void test01(){
        ProjectStatusEnum[] values = ProjectStatusEnum.values();
        for (ProjectStatusEnum projectStatusEnum:values) {
            System.out.println(projectStatusEnum.val);
            System.out.println(projectStatusEnum.name);
        }

        ProjectStatusEnum projectStatusEnum = ProjectStatusEnum.valueOf("NORMAL");
        System.out.println(projectStatusEnum.name);
    }

    public static void main(String[] args) {

        Cls[] cls = new Cls[]{ new Cls(1, 1), new Cls(1, 2), new Cls(2, 2) };
        System.out.println("Arrays.stream(cls).collect(Collectors.groupingBy(Cls::getA)) = "
                + Arrays.stream(cls).collect(Collectors.groupingBy(Cls::getA)));

    }

    @Data
    @AllArgsConstructor
    static class Cls {
        Integer a;
        Integer b;
    }
}
