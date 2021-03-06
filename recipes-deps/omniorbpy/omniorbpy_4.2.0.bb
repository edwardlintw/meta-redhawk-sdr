#
# This file is protected by Copyright. Please refer to the COPYRIGHT file distributed 
# with this source distribution.
#
# This file is part of Geon Technology's meta-redhawk-sdr.
#
# Geon Technology's meta-redhawk-sdr is free software: you can redistribute it and/or 
# modify it under the terms of the GNU Lesser General Public License as published by 
# the Free Software Foundation, either version 3 of the License, or (at your option) 
# any later version.
#
# Geon Technology's meta-redhawk-sdr is distributed in the hope that it will be useful, 
# but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
# details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program.  If not, see http://www.gnu.org/licenses/.
#

DESCRIPTION = "Python bindings for omniORB"
SECTION =  "devel"
PRIORITY = "optional"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=dcf3c825659e82539645da41a7908589"
DEPENDS += "omniorb omniorbpy-native python"
DEPENDS_virtclass-native += "omniorb-native python-native"

# This will allow this file to create symlinks to so files without throwing an error
INSANE_SKIP_${PN} = "dev-so"


SRC_URI_BASE = "http://downloads.sourceforge.net/omniorb/omniORBpy-4.2.0.tar.bz2;name=omniORBpytarbz2"
SRC_URI = "${SRC_URI_BASE}"
SRC_URI_virtclass-native = "${SRC_URI_BASE}"

SRC_URI[omniORBpytarbz2.md5sum] = "50ecde547c865aad2074d30224779412"
SRC_URI[omniORBpytarbz2.sha256sum] = "c82b3bafacbb93cfaace41765219155f2b24eb3781369bba0581feb1dc50fe5e"

PREFERRED_VERSION_omniorb = "4.2.0"

SRC_URI_append = "\
    file://omniORBpy-cross.patch \
    file://omniORBpy_modules_codesets_dir.mk.patch \
    file://omniORBpy_modules_connections_dir.mk.patch \
    file://omniORBpy_modules_sslTP_dir.mk.patch \
    file://omniORBpy_modules_dir.mk.patch \
    file://omniORBpy_modules_ziop_dir.mk.patch \
"

S = "${WORKDIR}/omniORBpy-${PV}"

EXTRA_OECONF = "--with-omniorb=${STAGING_EXECPREFIXDIR}"
EXTRA_OECONF_virtclass-native = "--with-omniorb=${exec_prefix}"

FILES_${PN} += " \
    ${libdir}/python2.7/site-packages/*.pth \
    ${libdir}/python2.7/site-packages/*.pyo \
    ${libdir}/python2.7/site-packages/*.py \
    ${libdir}/python2.7/site-packages/*.pyc \
    ${libdir}/python2.7/site-packages/*.so.* \
    ${libdir}/python2.7/site-packages/*.so \
    ${libdir}/python2.7/site-packages/omniidl_be/* \
    ${libdir}/python2.7/site-packages/omniORB/* \
    ${libdir}/python2.7/site-packages/omniORB/COS/* \
    ${libdir}/python2.7/site-packages/omniORB/COS/*/* \
    ${libdir}/python2.7/site-packages/CosNaming__POA/* \
    ${libdir}/python2.7/site-packages/CosNaming/* \
"

inherit autotools pkgconfig distutils-base

#ERROR: QA Issue with omniorbpy-dev: No GNU_HASH in the elf binary...
#See for more info: http://old.nabble.com/No-GNU_HASH-found-in-elf-binary-td23072960.html
TARGET_CC_ARCH += "${LDFLAGS}"

do_compile () {
    export OMNIORB_BINDIR=${STAGING_BINDIR_NATIVE}
    oe_runmake
}

do_compile_virtclass-native () {
    oe_runmake
}

# omniOrb already provides the init file
do_install_append() {
    rm -f ${D}${libdir}/python*/site-packages/omniidl_be/__init__.*
}

BBCLASSEXTEND = "native"

NATIVE_INSTALL_WORKS = "1"
