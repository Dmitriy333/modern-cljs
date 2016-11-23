(defproject modern-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; CLJ AND CLJS source code paths
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [compojure "1.4.0"] ;;vendor for ring's handler
                 [org.clojure/java.jdbc "0.6.1"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [hiccup "1.0.5"]
                 [javax.servlet/servlet-api "2.5"]          ;;repl doesn't work without dependencies
                 [ring/ring-json "0.4.0"]                   ;;to parse json https://github.com/ring-clojure/ring-json
                 [ring/ring-defaults "0.2.1"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [org.clojure/data.json "0.2.6"]            ;for json
                 [ring-basic-authentication "1.0.5"]
                 [digest "1.4.5"]                           ;; md5 cipher
                 ]


  :plugins [
            [lein-cljsbuild "1.1.1"],;; lein-cljsbuild plugin to build a CLJS project
            [lein-ring "0.9.7"]  ;; ring web-server
            [lein-figwheel "0.5.0-2"]
            ]

  ;; cljsbuild options configuration
  :cljsbuild {:builds
              [{
                ;; Google Closure (CLS) options configuration
                ;:compiler {;; CLS generated JS script filename
                ;           :output-to "resources/public/js/modern.js"
                ;
                ;           ;; minimal JS optimization directive
                ;           :optimizations :whitespace
                ;
                ;           ;; generated JS code prettyfication
                ;           :pretty-print true
                ;           }

                :source-paths ["src/cljs"] ;; CLJS source code path
                :figwheel true
                :compiler {  ;; Google Closure (CLS) options configuration
                           :output-to "resources/public/js/modern.js" ;; CLS generated JS script filename
                           :output-dir "resources/public/js/dev"
                           :optimizations :none ;; minimal JS optimization directive
                           :pretty-print true ;; generated JS code prettyfication
                           }

                }]
              }

  ;; configuration for ring-server
  :ring {:handler modern-cljs.core/handler}


  ;; to clean JS files generated during the build
  :clean-targets ^{:protect false} [:target-path "resources/public/js/"])