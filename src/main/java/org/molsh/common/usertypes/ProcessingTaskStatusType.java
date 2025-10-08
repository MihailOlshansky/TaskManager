package org.molsh.common.usertypes;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.molsh.common.ProcessingTaskStatus;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class ProcessingTaskStatusType implements UserType<ProcessingTaskStatus> {

    @Override
    public int getSqlType() {
        return Types.OTHER; // или Types.VARCHAR, если в базе хранится как строка
    }

    @Override
    public Class<ProcessingTaskStatus> returnedClass() {
        return ProcessingTaskStatus.class;
    }

    @Override
    public boolean equals(ProcessingTaskStatus x, ProcessingTaskStatus y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(ProcessingTaskStatus x) {
        return Objects.hashCode(x);
    }

    @Override
    public ProcessingTaskStatus nullSafeGet(ResultSet resultSet, int i, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws SQLException {
        return ProcessingTaskStatus.valueOf(resultSet.getString(i));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, ProcessingTaskStatus value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, value.name(), Types.OTHER);
        }
    }

    @Override
    public ProcessingTaskStatus deepCopy(ProcessingTaskStatus value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(ProcessingTaskStatus value) {
        return value;
    }

    @Override
    public ProcessingTaskStatus assemble(Serializable cached, Object owner) {
        return (ProcessingTaskStatus) cached;
    }


}
