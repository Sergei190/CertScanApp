package ru.KAA.Sergei.configuration;

import io.javalin.http.Context;

public class StaticFilesConfiguration {
    private String staticFilesPath;
    private String staticFilesLocation;

    public void setStaticFilesPath(String staticFilesPath) {
        this.staticFilesPath = staticFilesPath;
    }

    public void setStaticFilesLocation(String staticFilesLocation) {
        this.staticFilesLocation = staticFilesLocation;
    }

    public void apply(Context context) {
        String filePath = context.path().replace(staticFilesPath, "");
        String resourcePath = staticFilesLocation + filePath;

        if (resourceExists(resourcePath)) {
            context.contentType(getContentType(filePath));
            context.result(getClass().getResourceAsStream(resourcePath));
        } else {
            context.status(404);
        }
    }

    private boolean resourceExists(String resourcePath) {
        return getClass().getResource(resourcePath) != null;
    }

    private String getContentType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }
}
