package filecenter.dao;

import filecenter.model.AccessInfo;
import filecenter.model.AccessInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessInfoDao {
    long countByExample(AccessInfoExample example);

    int deleteByExample(AccessInfoExample example);

    int deleteByPrimaryKey(Integer accessId);

    int insert(AccessInfo record);

    int insertSelective(AccessInfo record);

    List<AccessInfo> selectByExample(AccessInfoExample example);

    AccessInfo selectByPrimaryKey(Integer accessId);

    int updateByExampleSelective(@Param("record") AccessInfo record, @Param("example") AccessInfoExample example);

    int updateByExample(@Param("record") AccessInfo record, @Param("example") AccessInfoExample example);

    int updateByPrimaryKeySelective(AccessInfo record);

    int updateByPrimaryKey(AccessInfo record);
}