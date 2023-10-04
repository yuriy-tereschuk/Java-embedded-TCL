
puts "TCL Filter Invoked!"

# Block request by the default!
set x 403

proc checkHeaders {} {
    # The first way to get value from GLOBAL context "upvar" reads value from global $x in local $code.
    upvar x code
    # The second way to expose variable name from GLOBAL context into local.
    global headers

    # Reading keys from associated array (hashtable) "headers"
    foreach k [array names headers] {

        puts "header: $k -> $headers($k)"

        if {$k == "authorized" && $headers($k) == "Ok"} {
            return 0
        }
    }
    return $code
}

# Verify correct method invocation, if no issues var "message" contains returned value from
# called procedure, otherwise error message returned from TCL interpreter.
# Try to change the name of "checkHeaders" procedure to see wat happens.
if {[catch {checkHeaders} message]} {
    puts "Error: $message"
} else {
    set x $message
    puts "x = $x"
}

if {$x == 0} {
    puts "pass"
    set result "pass"
} else {
    puts "drop"
    set result "drop"
}

return $result

