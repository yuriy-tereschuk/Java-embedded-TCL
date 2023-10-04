        Iterator<String> headerNames = rst.getHeaderNames();

        while (headerNames.hasNext()) {
            String headerName = headerNames.next();
            interp.setVar("headers", headerName, rst.getHeader(headerName), TCL.GLOBAL_ONLY);
        }


