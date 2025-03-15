package br.com.dio.persistence.converter;

import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class OffSetTimeConverter {
    public  static OffsetDateTime toOffSetDateTime(final Timestamp value){
        return nonNull(value) ? OffsetDateTime.ofInstant(value.toInstant(), UTC): null;
    }
}
