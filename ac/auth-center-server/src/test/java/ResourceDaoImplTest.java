import com.gapache.cloud.auth.server.AuthCenterServer;
import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.cloud.auth.server.dao.repository.resource.ResourceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/1/28 5:17 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthCenterServer.class)
public class ResourceDaoImplTest {

    @Resource
    private ResourceRepository resourceRepository;

    @Test
    public void findAllResource() {
        List<ResourceEntity> allCustomize = resourceRepository.findAllResource(0L);
        System.out.println(allCustomize);
    }

    @Test
    public void findResourceCustomizeById() {
        ResourceEntity entity = resourceRepository.findResourceCustomizeById(1L);
        System.out.println(entity);
    }
}
