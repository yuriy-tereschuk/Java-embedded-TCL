proc checkHeaders {} {
    global headers
    foreach k [array names headers] {

        puts "header: $k -> $headers($k)"

        if {$k == "authorized" && $headers($k) == "Ok"} {
            return 1
        }
    }
    return 0
}

set x [checkHeaders]


