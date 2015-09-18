# DeepFractal

A fractal explorer webapp

## Planned features

* Fast rendering with webgl 
* Smooth coloring
* Color editor using d3 graphs
* Fast deep zooming using a perturbation algorithm
* High quality final rendering, with anti-aliasing

## Development

Deepfractal is written in clojurescript using  [re-frame](https://github.com/Day8/re-frame).


To start live editing with figwheel and garden, run:

```
lein dev
```

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).


### Emacs

For setting up a repl and dev tools in emacs, use can use cider. To quickly connect you can add the following commands to your .emacs:  

```elisp

(defun cider-start-browser-repl ()
  "Start a browser repl"
  (interactive)
  (let ((buffer (cider-current-repl-buffer)))
    (cider-eval "(do (use 'figwheel-sidecar.repl-api) (cljs-repl))"
                (cider-insert-eval-handler buffer)
                (cider-current-ns))
    (remove-hook 'cider-connected-hook 'cider-start-browser-repl)))
 
 (defun clojurescript-repl ()
  (interactive)
  (add-hook 'cider-connected-hook 'cider-start-browser-repl)
  (cider-connect "localhost" 7888))
 ```
 
 Then
 ```
M-x clojurescript-repl
```
opens up the repl.


### Run tests:

```
lein clean
lein cljsbuild auto test
```

## Production Build

```
lein clean
lein cljsbuild once min
```
