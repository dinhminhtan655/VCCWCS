/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wcs.vcc.retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A generic class that holds loading status.
 */
public class Resource {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    public final int resultCount;

    private Resource(@NonNull Status status, @Nullable String message, int resultCount) {
        this.status = status;
        this.message = message;
        this.resultCount = resultCount;
    }

    public static Resource success(int resultCount) {
        return new Resource(Status.SUCCESS, null, resultCount);
    }

    public static Resource success() {
        return new Resource(Status.SUCCESS, null, 1);
    }

    public static Resource error(String msg) {
        return new Resource(Status.ERROR, msg, 1);
    }

    public static Resource loading() {
        return new Resource(Status.LOADING, null, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource resource = (Resource) o;

        return status == resource.status && (message != null ? message.equals(resource.message) : resource.message == null);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message +
                '}';
    }
}
