/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iceberg.rest.responses;

import java.util.Map;
import org.apache.iceberg.TableMetadata;
import org.apache.iceberg.relocated.com.google.common.base.MoreObjects;
import org.apache.iceberg.relocated.com.google.common.base.Preconditions;
import org.apache.iceberg.relocated.com.google.common.collect.ImmutableMap;
import org.apache.iceberg.relocated.com.google.common.collect.Maps;
import org.apache.iceberg.rest.RESTResponse;

/**
 *
 */
public class LoadTableResponse implements RESTResponse {

  private String metadataLocation;
  private TableMetadata meta;
  private Map<String, String> config;

  public LoadTableResponse() {
    // Required for Jackson deserialization
  }

  private LoadTableResponse(String metadataLocation, TableMetadata meta, Map<String, String> config) {
    this.metadataLocation = metadataLocation;
    this.meta = meta;
    this.config = config;
  }

  @Override
  public void validate() {
  }

  public String metadataLocation() {
    return metadataLocation;
  }

  public TableMetadata tableMetadata() {
    return TableMetadata.buildFrom(meta).withMetadataLocation(metadataLocation).build();
  }

  public Map<String, String> config() {
    return config != null ? config : ImmutableMap.of();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("metadata", meta)
        .add("config", config)
        .toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String metadataLocation;
    private TableMetadata meta;
    private Map<String, String> config = Maps.newHashMap();

    private Builder() {
    }

    public Builder withTableMetadata(TableMetadata metadata) {
      this.metadataLocation = metadata.metadataFileLocation();
      this.meta = metadata;
      return this;
    }

    public Builder addConfig(String property, String value) {
      config.put(property, value);
      return this;
    }

    public Builder addAllConfig(Map<String, String> properties) {
      config.putAll(properties);
      return this;
    }

    public LoadTableResponse build() {
      Preconditions.checkNotNull(meta, "Invalid metadata: null");
      return new LoadTableResponse(metadataLocation, meta, config);
    }
  }
}
