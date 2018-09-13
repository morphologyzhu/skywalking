/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.core.analysis.manual.servicerelation;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.Const;
import org.apache.skywalking.oap.server.core.analysis.indicator.Indicator;
import org.apache.skywalking.oap.server.core.analysis.indicator.annotation.IndicatorType;
import org.apache.skywalking.oap.server.core.remote.annotation.StreamData;
import org.apache.skywalking.oap.server.core.remote.grpc.proto.RemoteData;
import org.apache.skywalking.oap.server.core.storage.StorageBuilder;
import org.apache.skywalking.oap.server.core.storage.annotation.Column;
import org.apache.skywalking.oap.server.core.storage.annotation.IDColumn;
import org.apache.skywalking.oap.server.core.storage.annotation.StorageEntity;

@IndicatorType
@StreamData
@StorageEntity(name = ServiceCallRelationIndicator.INDEX_NAME, builder = ServiceCallRelationIndicator.Builder.class)
public class ServiceCallRelationIndicator extends Indicator {

    public static final String INDEX_NAME = "service_call_relation";
    public static final String SOURCE_SERVICE_ID = "source_service_id";
    public static final String DEST_SERVICE_ID = "dest_service_id";

    @Setter @Getter @Column(columnName = SOURCE_SERVICE_ID) @IDColumn private int sourceServiceId;
    @Setter @Getter @Column(columnName = DEST_SERVICE_ID) @IDColumn private int destServiceId;

    @Override public String id() {
        String splitJointId = String.valueOf(getTimeBucket());
        splitJointId += Const.ID_SPLIT + String.valueOf(sourceServiceId);
        splitJointId += Const.ID_SPLIT + String.valueOf(destServiceId);
        return splitJointId;
    }

    @Override public void combine(Indicator indicator) {

    }

    @Override public void calculate() {

    }

    @Override public Indicator toHour() {
        ServiceCallRelationIndicator indicator = new ServiceCallRelationIndicator();
        indicator.setTimeBucket(toTimeBucketInHour());
        indicator.setSourceServiceId(getSourceServiceId());
        indicator.setDestServiceId(getDestServiceId());
        return indicator;
    }

    @Override public Indicator toDay() {
        ServiceCallRelationIndicator indicator = new ServiceCallRelationIndicator();
        indicator.setTimeBucket(toTimeBucketInDay());
        indicator.setSourceServiceId(getSourceServiceId());
        indicator.setDestServiceId(getDestServiceId());
        return indicator;
    }

    @Override public Indicator toMonth() {
        ServiceCallRelationIndicator indicator = new ServiceCallRelationIndicator();
        indicator.setTimeBucket(toTimeBucketInMonth());
        indicator.setSourceServiceId(getSourceServiceId());
        indicator.setDestServiceId(getDestServiceId());
        return indicator;
    }

    @Override public int remoteHashCode() {
        int result = 17;
        result = 31 * result + sourceServiceId;
        result = 31 * result + destServiceId;
        return result;
    }

    @Override public void deserialize(RemoteData remoteData) {
        setSourceServiceId(remoteData.getDataIntegers(0));
        setDestServiceId(remoteData.getDataIntegers(1));
        setTimeBucket(remoteData.getDataLongs(0));
    }

    @Override public RemoteData.Builder serialize() {
        RemoteData.Builder remoteBuilder = RemoteData.newBuilder();

        remoteBuilder.setDataIntegers(0, getSourceServiceId());
        remoteBuilder.setDataIntegers(1, getDestServiceId());
        remoteBuilder.setDataLongs(0, getTimeBucket());

        return remoteBuilder;
    }

    @Override public int hashCode() {
        int result = 17;
        result = 31 * result + sourceServiceId;
        result = 31 * result + destServiceId;
        result = 31 * result + (int)getTimeBucket();
        return result;
    }

    @Override public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        ServiceCallRelationIndicator indicator = (ServiceCallRelationIndicator)obj;
        if (sourceServiceId != indicator.sourceServiceId)
            return false;
        if (destServiceId != indicator.destServiceId)
            return false;

        if (getTimeBucket() != indicator.getTimeBucket())
            return false;

        return true;
    }

    public static class Builder implements StorageBuilder<ServiceCallRelationIndicator> {

        @Override public ServiceCallRelationIndicator map2Data(Map<String, Object> dbMap) {
            ServiceCallRelationIndicator indicator = new ServiceCallRelationIndicator();
            indicator.setSourceServiceId(((Number)dbMap.get(SOURCE_SERVICE_ID)).intValue());
            indicator.setDestServiceId(((Number)dbMap.get(DEST_SERVICE_ID)).intValue());
            indicator.setTimeBucket(((Number)dbMap.get(TIME_BUCKET)).longValue());
            return indicator;
        }

        @Override public Map<String, Object> data2Map(ServiceCallRelationIndicator storageData) {
            Map<String, Object> map = new HashMap<>();
            map.put(SOURCE_SERVICE_ID, storageData.getSourceServiceId());
            map.put(DEST_SERVICE_ID, storageData.getDestServiceId());
            map.put(TIME_BUCKET, storageData.getTimeBucket());
            return map;
        }
    }
}