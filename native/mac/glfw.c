#define _GLFW_COCOA
#define _GLFW_NSGL
#define _GLFW_USE_OPENGL

//common
#include "../glfw/src/context.c"
#include "../glfw/src/init.c"
#include "../glfw/src/input.c"
#include "../glfw/src/monitor.c"
#include "../glfw/src/window.c"

//mac
#include "../glfw/src/cocoa_init.c"
#include "../glfw/src/cocoa_monitor.c"
#include "../glfw/src/mach_time.c"
#include "../glfw/src/posix_tls.c"
#include "../glfw/src/cocoa_window.c"
#include "../glfw/src/iokit_joystick.c"
#include "../glfw/src/nsgl_context.m"
