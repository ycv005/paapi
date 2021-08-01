#import "PaapiPlugin.h"
#if __has_include(<paapi/paapi-Swift.h>)
#import <paapi/paapi-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "paapi-Swift.h"
#endif

@implementation PaapiPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPaapiPlugin registerWithRegistrar:registrar];
}
@end
