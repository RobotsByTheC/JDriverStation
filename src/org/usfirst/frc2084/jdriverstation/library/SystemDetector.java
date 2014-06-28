package org.usfirst.frc2084.jdriverstation.library;

/**
 * A small class to determine the native system's operating system family and
 * architecture.
 *
 * @author Alexander Barker
 * @author Ben Wolsieffer
 */
public class SystemDetector {

    /**
     * The operating system family enum.
     *
     * @see SystemDetector
     */
    public enum Family {

        /**
         * The FreeBSD operating system family.
         */
        FREEBSD("FreeBSD"),
        /**
         * The OpenBSD operating system family.
         */
        OPENBSD("OpenBSD"),
        /**
         * The Apple Mac OS X operating system family.
         */
        DARWIN("Mac OS X"),
        /**
         * The Solaris operating system family.
         */
        SOLARIS("Solaris"),
        /**
         * The Linux operating system family.
         */
        LINUX("Linux"),
        /**
         * The Windows operating system family.
         */
        WINDOWS("Windows"),
        /**
         * Any unsupported operating system family.
         */
        UNKNOWN("Unknown");

        private final String name;

        private Family(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

    }

    /**
     * The system architecture enum.
     *
     * @see SystemDetector
     */
    public enum Architecture {

        /**
         * The alpha architecture.
         */
        ALPHA("Alpha"),
        /**
         * The arm architecture.
         */
        ARM("Arm"),
        /**
         * The itanium64 32-bit architecture.
         */
        IA64_32("Itanium (32 bit) or whatever"),
        /**
         * The itanium64 architecture.
         */
        IA64("Itanium (64 bit)"),
        /**
         * The mips architecture.
         */
        MIPS("MIPS"),
        /**
         * The sparc architecture.
         */
        SPARC("SPARC (32 bit)"),
        /**
         * The sparc64 architecture.
         */
        SPARC64("SPARC (64 bit)"),
        /**
         * The ppc architecture.
         */
        PPC("PowerPC (32 bit)"),
        /**
         * The ppc64 architecture.
         */
        PPC64("PowerPC (64 bit)"),
        /**
         * The x86 architecture.
         */
        x86("x86"),
        /**
         * The amd64 architecture.
         */
        x86_64("x86_64"),
        /**
         * Any unsupported system architecture.
         */
        UNKNOWN("Unknown");

        private final String name;

        private Architecture(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    /**
     * Determines the current operating system family.
     *
     * @return The current operating system family enum item.
     */
    public static Family getFamily() {
        String osName = System.getProperty("os.name").toLowerCase();

        switch (osName) {
            case "freebsd":
                return Family.FREEBSD;
            case "openbsd":
                return Family.OPENBSD;
            case "mac os x":
                return Family.DARWIN;
            case "solaris":
            case "sunos":
                return Family.SOLARIS;
            case "linux":
                return Family.LINUX;
            default:
                if (osName.startsWith("windows")) {
                    return Family.WINDOWS;
                } else {
                    return Family.UNKNOWN;
                }
        }
    }

    /**
     * Determines the current system architecture.
     *
     * @return The current system architecture.
     */
    public static Architecture getArchitecture() {
        String osArch = System.getProperty("os.arch").toLowerCase();

        switch (osArch) {
            case "alpha":
                return Architecture.ALPHA;
            case "ia64_32":
                return Architecture.IA64_32;
            case "ia64":
                return Architecture.IA64;
            case "mips":
                return Architecture.MIPS;
            case "sparc":
                return Architecture.MIPS;
            case "ppc":
            case "powerpc":
                return Architecture.PPC;
            case "ppc64":
            case "powerpc64":
                return Architecture.PPC64;
            case "x86":
            case "i368":
            case "i486":
            case "i585":
            case "i686":
                return Architecture.x86;
            case "x86_64":
            case "amd64":
            case "k8":
                return Architecture.x86_64;
            default:
                if (osArch.startsWith("arm")) {
                    return Architecture.ARM;
                } else {
                    return Architecture.UNKNOWN;
                }
        }
    }
}
