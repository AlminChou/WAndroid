package com.almin.wandroid.data.network.converter.type;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;
final class MoshiRequestBodyConverter<T> implements Converter<T, RequestBody> {
  private static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8");

  private final JsonAdapter<T> adapter;

  MoshiRequestBodyConverter(JsonAdapter<T> adapter) {
    this.adapter = adapter;
  }

  @Override
  public RequestBody convert(T value) throws IOException {
    Buffer buffer = new Buffer();
    JsonWriter writer = JsonWriter.of(buffer);
    adapter.toJson(writer, value);
    return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
  }
}
