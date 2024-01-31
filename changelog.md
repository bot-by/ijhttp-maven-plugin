# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased
### Added
- jUnit Extension
### Updated
- Update dependencies

## 1.2.1 - 2024-01-18
### Fixed
- Deprecated methods in `commons-exec`.
### Updated
- Update dependencies

## 1.2.0 - 2023-12-30
### Added
- Directories
### Updated
- Updated Spring Boot to 3.2.1

## 1.1.1 - 2023-12-20
### Security
- CVE-2023-6378: A serialization vulnerability in logback receiver component part of logback version 1.4.11 allows an attacker to mount a Denial-Of-Service attack by sending poisoned data.

## 1.1.0 - 2023-12-20
### Added
- The proxy feature
- The report path
- Split Maven plugin and CLI builder modules
- The Spring Boot Test autoconfiguration

## 1.0.1 - 2023-11-08
### Fixed
- Environment variables, public and private, are added without handling quoting

## 1.0.0 - 2023-05-05
### Added
- Executor Mojo: reads the configuration then runs `ijhttp`.