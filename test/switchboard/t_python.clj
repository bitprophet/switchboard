(ns switchboard.t-python
  (:require [midje.sweet :refer :all]
            [org.httpkit.fake :refer [with-fake-http]]
            [switchboard.t-utils :refer :all]))


(defn py [x] (redirect (str "https://docs.python.org" x)))
(defn pp [x] (redirect (str "https://pypi.org" x)))


(facts "about basic behavior"

  (fact "bare 'py' key just hits 3.x stdlib landing page"
        (query "py") => (py "/3/library"))

  (with-fake-http ["https://docs.python.org/3/library/operator.html" "ok"]
    (fact "direct module name hits become straight redirects"
          (query "py operator") => (py "/3/library/operator.html")))

  (fact "non module name hits become generic searches"
        (query "py lol wut") => (py "/3/search.html?q=lol wut")))


(facts "about different Python versions"

  (fact "py2 uses Python 2.7 docs"
        (query "py2 os") => (py "/2.7/library/os.html"))

  (fact "py36 uses Python 3.6 docs"
       (query "py36 re") => (py "/3.6/library/re.html"))

  (fact "py37 uses Python 3.7 docs"
       (query "py37 re") => (py "/3.7/library/re.html")))


(facts "regarding pypi search"

  (fact "bare 'pp' key just goes to pypi landing page"
        (query "pp") => (pp ""))

  (with-fake-http ["https://pypi.org/project/paramiko" "ok"
                   "https://pypi.org/project/lol" 404]
    (fact "direct package name hit goes straight there"
          (query "pp paramiko") => (pp "/project/paramiko"))

    (fact "'pp' with a non-exact-matching argument searches pypi"
          (query "pp lol") => (pp "/search/?q=lol"))))
