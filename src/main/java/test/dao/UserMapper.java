package test.dao;

import org.mybatis.anno.Select;
import test.pojo.User;

public interface UserMapper {
    @Select(sql = "select lfPartyId,partyName from LfParty where lfPartyId = #{id}")
    User queryUserById(int id);
    @Select(sql = "select lfPartyId,partyName from LfParty where lfPartyId = #{id} and partyName = #{name}")
    User queryUserByIdAndName(int id,String name);
}
