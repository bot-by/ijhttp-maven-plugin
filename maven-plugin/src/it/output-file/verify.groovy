File log = new File(basedir, "target/logs/simple-run.log")
File report = new File(basedir, "reports/report.xml")

assert log.file: "log"
assert report.file: "report"
