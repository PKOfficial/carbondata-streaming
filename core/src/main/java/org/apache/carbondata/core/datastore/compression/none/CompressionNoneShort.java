/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.carbondata.core.datastore.compression.none;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

import org.apache.carbondata.common.logging.LogService;
import org.apache.carbondata.common.logging.LogServiceFactory;
import org.apache.carbondata.core.datastore.chunk.store.MeasureChunkStoreFactory;
import org.apache.carbondata.core.datastore.chunk.store.MeasureDataChunkStore;
import org.apache.carbondata.core.datastore.compression.Compressor;
import org.apache.carbondata.core.datastore.compression.CompressorFactory;
import org.apache.carbondata.core.datastore.compression.ValueCompressionHolder;
import org.apache.carbondata.core.metadata.datatype.DataType;
import org.apache.carbondata.core.util.ValueCompressionUtil;

public class CompressionNoneShort extends ValueCompressionHolder<short[]> {
  /**
   * Attribute for Carbon LOGGER
   */
  private static final LogService LOGGER =
      LogServiceFactory.getLogService(CompressionNoneShort.class.getName());

  /**
   * shortCompressor.
   */
  private static Compressor compressor = CompressorFactory.getInstance().getCompressor();

  /**
   * value.
   */
  private short[] shortValue;

  private MeasureDataChunkStore<short[]> measureChunkStore;

  private DataType actualDataType;

  public CompressionNoneShort(DataType actualDataType) {
    this.actualDataType = actualDataType;
  }

  @Override public void setValue(short[] shortValue) {
    this.shortValue = shortValue;
  }

  @Override public short[] getValue() {
    return this.shortValue;
  }

  @Override
  public void uncompress(DataType dataType, byte[] data, int offset, int length, int decimalPlaces,
      Object maxValueObject, int numberOfRows) {
    super.unCompress(compressor, dataType, data, offset, length, numberOfRows, maxValueObject,
        decimalPlaces);
  }

  @Override public void compress() {
    compressedValue = super.compress(compressor, DataType.SHORT, shortValue);
  }

  @Override public void setValueInBytes(byte[] value) {
    ByteBuffer buffer = ByteBuffer.wrap(value);
    shortValue = ValueCompressionUtil.convertToShortArray(buffer, value.length);
  }

  @Override public long getLongValue(int index) {
    return measureChunkStore.getShort(index);
  }

  @Override public double getDoubleValue(int index) {
    return measureChunkStore.getShort(index);
  }

  @Override public BigDecimal getBigDecimalValue(int index) {
    throw new UnsupportedOperationException("Big decimal is not defined for CompressionNonShort");
  }

  @Override public void freeMemory() {
    this.measureChunkStore.freeMemory();
  }

  @Override
  public void setValue(short[] data, int numberOfRows, Object maxValueObject, int decimalPlaces) {
    this.measureChunkStore = MeasureChunkStoreFactory.INSTANCE
        .getMeasureDataChunkStore(DataType.SHORT, numberOfRows);
    this.measureChunkStore.putData(data);
  }
}
