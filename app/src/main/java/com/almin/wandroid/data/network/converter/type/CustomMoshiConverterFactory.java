package com.almin.wandroid.data.network.converter.type;

import static java.util.Collections.unmodifiableSet;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} which uses Moshi for JSON.
 *
 * <p>Because Moshi is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must {@linkplain Retrofit.Builder#addConverterFactory(Converter.Factory) add this
 * instance} last to allow the other converters a chance to see their types.
 *
 * <p>Any {@link JsonQualifier @JsonQualifier}-annotated annotations on the parameter will be used
 * when looking up a request body converter and those on the method will be used when looking up a
 * response body converter.
 */
public final class CustomMoshiConverterFactory extends Converter.Factory {
  /** Create an instance using a default {@link Moshi} instance for conversion. */
  public static CustomMoshiConverterFactory create() {
    return create(new Moshi.Builder().build());
  }

  /** Create an instance using {@code moshi} for conversion. */
  @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
  public static CustomMoshiConverterFactory create(Moshi moshi) {
    if (moshi == null) throw new NullPointerException("moshi == null");
    return new CustomMoshiConverterFactory(moshi, false, false, false);
  }

  private final Moshi moshi;
  private final boolean lenient;
  private final boolean failOnUnknown;
  private final boolean serializeNulls;

  private CustomMoshiConverterFactory(
      Moshi moshi, boolean lenient, boolean failOnUnknown, boolean serializeNulls) {
    this.moshi = moshi;
    this.lenient = lenient;
    this.failOnUnknown = failOnUnknown;
    this.serializeNulls = serializeNulls;
  }

  /** Return a new factory which uses {@linkplain JsonAdapter#lenient() lenient} adapters. */
  public CustomMoshiConverterFactory asLenient() {
    return new CustomMoshiConverterFactory(moshi, true, failOnUnknown, serializeNulls);
  }

  /** Return a new factory which uses {@link JsonAdapter#failOnUnknown()} adapters. */
  public CustomMoshiConverterFactory failOnUnknown() {
    return new CustomMoshiConverterFactory(moshi, lenient, true, serializeNulls);
  }

  /** Return a new factory which includes null values into the serialized JSON. */
  public CustomMoshiConverterFactory withNullSerialization() {
    return new CustomMoshiConverterFactory(moshi, lenient, failOnUnknown, true);
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(
      Type type, Annotation[] annotations, Retrofit retrofit) {
    JsonAdapter<?> adapter = moshi.adapter(type, jsonAnnotations(annotations));
    if (lenient) {
      adapter = adapter.lenient();
    }
    if (failOnUnknown) {
      adapter = adapter.failOnUnknown();
    }
    if (serializeNulls) {
      adapter = adapter.serializeNulls();
    }
    return new CustomMoshiResponseBodyConverter<>(adapter, moshi);
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(
      Type type,
      Annotation[] parameterAnnotations,
      Annotation[] methodAnnotations,
      Retrofit retrofit) {
    JsonAdapter<?> adapter = moshi.adapter(type, jsonAnnotations(parameterAnnotations));
    if (lenient) {
      adapter = adapter.lenient();
    }
    if (failOnUnknown) {
      adapter = adapter.failOnUnknown();
    }
    if (serializeNulls) {
      adapter = adapter.serializeNulls();
    }
    return new MoshiRequestBodyConverter<>(adapter);
  }

  private static Set<? extends Annotation> jsonAnnotations(Annotation[] annotations) {
    Set<Annotation> result = null;
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().isAnnotationPresent(JsonQualifier.class)) {
        if (result == null) result = new LinkedHashSet<>();
        result.add(annotation);
      }
    }
    return result != null ? unmodifiableSet(result) : Collections.<Annotation>emptySet();
  }
}
