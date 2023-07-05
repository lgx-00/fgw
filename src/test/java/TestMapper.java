import com.pxxy.FGWApplication;
import com.pxxy.enums.ProjectStatusEnum;
import com.pxxy.mapper.ProjectMapper;
import com.pxxy.pojo.Project;
import com.pxxy.utils.RandomTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: hesen
 * @Date: 2023-06-29-13:30
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

        List<Project> projectList = projectMapper.getVagueProjectByUser(2, null, 113, "萍乡", null, null, null, null);
        System.out.println(projectList);
        System.out.println(projectList.size());
    }
    @Test
    public void test01(){
        ProjectStatusEnum[] values = ProjectStatusEnum.values();
        for (ProjectStatusEnum projectStatusEnum:values) {
            System.out.println(projectStatusEnum.getStatus());
            System.out.println(projectStatusEnum.getStatusContent());
        }

        ProjectStatusEnum projectStatusEnum = ProjectStatusEnum.valueOf("NORMAL");
        System.out.println(projectStatusEnum.getStatusContent());
    }

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 3; i++) {
            String s = RandomTokenUtil.generate(1);
            System.out.println(s);
            Thread.sleep(500);
            System.out.println(RandomTokenUtil.verifyAndRefresh(s, 1));
            Thread.sleep(500);
            System.out.println(RandomTokenUtil.verifyAndRefresh(s, 1));
            Thread.sleep(1500);
            System.out.println(RandomTokenUtil.verifyAndRefresh(s, 1));
        }
    }
}
