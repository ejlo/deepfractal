(defproject deepfractal "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [tailrecursion/cljs-priority-map "1.1.0"]
                 [reagent "0.5.1"]
                 [re-frame "0.4.1"]
                 [garden "1.3.0-SNAPSHOT"]
                 [secretary "1.2.3"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.3.9" :exclusions [cider/cider-nrepl]]
            [lein-garden "0.2.6"]
            [lein-pdo "0.1.1"]
            [lein-ancient "0.6.7"]
            [lein-npm "0.6.1"]
            #_[lein-bower "0.5.1"]
            [lein-shell "0.4.1"]]


  :npm {:dependencies [[serve-static "1.10.0"]
                       [express "4.13.3"]
]}

  :bower-dependencies [[bootstrap-css-only "3.3.5"]]

  :bower {:directory "bower_components"}

  :clean-targets ^{:protect false} ["target" "test/js"
                                    "resources/public/js/compiled"
                                    "resources/public/css/compiled"
                                    "resources/server/js/compiled"]

  :aliases {;; development
            "css" ["garden" "auto"]
            "fig" ["figwheel" "dev"]
            "auto" ["pdo" "css," "fig"]
            "dev" ["do" "clean," "auto"]

            ;; production
            "css-prod" ["garden" "once"]
            "js-prod" ["cljsbuild" "once" "prod"]
            "server" ["cljsbuild" "once" "server"]
            "server-auto" ["cljsbuild" "auto" "server"]
            "prod" ["do" "clean," "css-prod," "js-prod," "server"]
            "start-server" ["shell" "node" "resources/server/js/compiled/server.js"]
            "prod-start" ["do"  "prod," "server," "start-server,"]

            ;; install & upgrade
            "bower-install" ["do" "bower" "install," "shell" "scripts/install.sh"]
            "npm-install" ["npm" "install"]
            "c-install" ["shell" "scripts/install_arprec_wrapper.sh"]
            "install-deps" ["do" "deps," "npm-install," "bower-install," "c-install"]
            "check-deps" ["ancient" ":all"]
            "upgrade-deps" ["ancient" "upgrade" ":all"]}

  :garden {:builds [{:id "screen"
                     :source-paths ["src/clj"]
                     :stylesheet deepfractal.css/screen
                     :compiler {:output-to "resources/public/css/compiled/screen.css"
                                :pretty-print? true}}]}

  :figwheel {:css-dirs ["resources/public/css/compiled"]
             :repl false
             :nrepl-port 7888}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]

                        :figwheel {:on-jsload "deepfractal.core/mount-root"}

                        :compiler {:main deepfractal.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :notify-command ["phantomjs" "test/unit-test.js" "test/unit-test.html"]
                        :compiler {:optimizations :whitespace
                                   :pretty-print true
                                   :output-to "test/js/app_test.js"
                                   :warnings {:single-segment-namespace false}}}

                       {:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:main deepfractal.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false}}


                       {:id "server"
                        :source-paths ["src/server"]
                        :compiler {:main deepfractal.server.core
                                   :output-to "resources/server/js/compiled/server.js"
                                   :optimizations :simple
                                   :target :nodejs}}
                       ]})
