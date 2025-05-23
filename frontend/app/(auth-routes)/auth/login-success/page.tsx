'use client';

import React, { useEffect, useState } from 'react';
import { ShieldCheck } from 'lucide-react';
import { useRouter } from 'next/navigation';

export default function LoginSuccessPage() {
  const [counter, setCounter] = useState(3);
  const router = useRouter();

  useEffect(() => {
    const interval = setInterval(() => {
      setCounter((prevCounter) => prevCounter - 1);
      if (counter <= 1) {
        router.push('/workspace');
      }
    }, 1000);
    return () => clearInterval(interval);
  }, [counter, router]);

  return (
    <div className="flex min-h-svh justify-center items-center flex-col gap-8">
      <ShieldCheck className="text-9xl text-primary" />
      <h2 className="text-4xl font-bold">You&apos;re logged in!</h2>
      <p className="text-lg">
        Redirecting to workspace in {counter} seconds...
      </p>
    </div>
  );
}
