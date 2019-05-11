package filecenter.dao;

import filecenter.model.CategoryDetil;
import filecenter.model.CategoryDetilExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDetilDao {
    long countByExample(CategoryDetilExample example);

    int deleteByExample(CategoryDetilExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CategoryDetil record);

    int insertSelective(CategoryDetil record);

    List<CategoryDetil> selectByExample(CategoryDetilExample example);

    CategoryDetil selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CategoryDetil record, @Param("example") CategoryDetilExample example);

    int updateByExample(@Param("record") CategoryDetil record, @Param("example") CategoryDetilExample example);

    int updateByPrimaryKeySelective(CategoryDetil record);

    int updateByPrimaryKey(CategoryDetil record);
    int insertAndGetId(CategoryDetil record);
}