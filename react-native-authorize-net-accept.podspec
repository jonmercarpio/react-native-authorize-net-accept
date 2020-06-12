require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-authorize-net-accept"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-authorize-net-accept
                   DESC
  s.homepage     = "https://github.com/jonmercarpio/react-native-authorize-net-accept"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Jonmer Carpio" => "jonmer09@gmail.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/jonmercarpio/react-native-authorize-net-accept.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.swift_version = '4.2'
  s.dependency "React"
  s.dependency "AuthorizeNetAccept"
  # ...
  # s.dependency "..."
end

